package me.bttf.smartstore.controller.api.seller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bttf.smartstore.auth.CustomUserDetails;
import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductOption;
import me.bttf.smartstore.domain.product.ProductStatus;
import me.bttf.smartstore.domain.store.Store;
import me.bttf.smartstore.dto.inventory.InvReq;
import me.bttf.smartstore.dto.product.ProductListRes;
import me.bttf.smartstore.dto.seller.ProductCreateReq;
import me.bttf.smartstore.dto.seller.ProductUpdateReq;
import me.bttf.smartstore.dto.seller.SellerDashboardRes;
import me.bttf.smartstore.repository.InventoryTxRepository;
import me.bttf.smartstore.repository.ProductOptionRepository;
import me.bttf.smartstore.repository.ProductRepository;
import me.bttf.smartstore.repository.StoreRepository;
import me.bttf.smartstore.service.InventoryService;
import me.bttf.smartstore.service.ProductCategoryService;
import me.bttf.smartstore.service.SellerProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
public class SellerDashboardController {

    private final ProductRepository productRepo;
    private final ProductOptionRepository optionRepo;
    private final ProductCategoryService pcService;
    private final StoreRepository storeRepo;
    private final InventoryService inventoryService;
    private final SellerProductService sellerProductService;

    // 10. 내 상품 관리 (/seller/dashboard) - 간단 집계
    @GetMapping("/dashboard")
    public SellerDashboardRes dashboard() {
        long productCount = productRepo.count();
        long lowStockCount = optionRepo.countByStockQtyLessThan(5); // 임계치 5, 필요시 파라미터로
        long orderCount = 0L;
        return new SellerDashboardRes(productCount, orderCount, lowStockCount);
    }

    // 11. 상품 등록 (/seller/products/new)
    @PostMapping("/products/new")
    @Operation(summary = "상품 등록")
    @Transactional
    public ProductListRes createProduct(
            @RequestBody ProductCreateReq req,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Long memberId = principal.getMember().getId();
        log.info("[CREATE_PRODUCT] called by memberId={}", memberId);

        Store store = storeRepo.findByOwner_Id(memberId)
                .orElseThrow(() -> new IllegalStateException("판매자 스토어가 없습니다."));
        log.info("[CREATE_PRODUCT] resolved storeId={}", store.getId());

        Product product = Product.builder()
                .store(store)
                .name(req.name())
                .status(ProductStatus.INACTIVE)
                .build();
        productRepo.save(product);
        log.info("[CREATE_PRODUCT] saved productId={}, storeId={}", product.getId(), store.getId());

        pcService.assign(product.getId(), req.categoryId(), req.primary());

        return ProductListRes.from(product);
    }

    // 12. 상품 수정 (/seller/products/{id}/edit)
    @PatchMapping("/products/{id}/edit")
    @Operation(summary = "상품 수정")
    @Transactional
    public ResponseEntity<ProductListRes> editProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateReq req) {

        Product p = productRepo.findById(id).orElseThrow();

        if (req.name() != null && !req.name().isBlank()) { p.changeName(req.name());}
        if (req.status() != null) { p.changeStatus(req.status());}

        return ResponseEntity.ok(ProductListRes.from(p));
    }

    // 13. 재고 관리 (/seller/inventory) - SKU 목록 페이징
    @GetMapping("/inventory")
    @Operation(summary = "재고 관리")
    @Transactional(readOnly = true)
    public Page<ProductOption> listSkus(@RequestParam(required = false) Long productId,
                                        @PageableDefault(size = 20) Pageable pageable) {
        if (productId != null) {
            return optionRepo.findByProduct_Id(productId, pageable);
        }
        return optionRepo.findAll(pageable);
    }

    // 재고 입고
    @PostMapping("/inventory/{optionId}/in")
    @Operation(summary = "재고 입고")
    public ResponseEntity<Void> stockIn(@PathVariable Long optionId, @RequestBody @Valid InvReq req) {
        inventoryService.in(optionId, req.qty(), req.reason());
        return ResponseEntity.ok().build();
    }

    // 재고 출고
    @PostMapping("/inventory/{optionId}/out")
    @Operation(summary = "재고 출고")
    public ResponseEntity<Void> stockOut(@PathVariable Long optionId, @RequestBody @Valid InvReq req) {
        inventoryService.out(optionId, req.qty(), req.reason(), null);
        return ResponseEntity.ok().build();
    }

    // 재고 조정
    @PostMapping("/inventory/{optionId}/adjust")
    @Operation(summary = "재고 조정")
    public ResponseEntity<Void> stockAdjust(@PathVariable Long optionId, @RequestBody @Valid InvReq req) {
        inventoryService.adjust(optionId, req.qty(), req.reason());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/set-qty")
    @Operation(summary = "재고 수량 절대값 설정")
    @Transactional
    public ResponseEntity<Void> setQty(@RequestBody @Valid InvReq req,
                                       @AuthenticationPrincipal CustomUserDetails me) {

        // 로그인한 판매자(owner)의 ID
        long ownerId = me.getMember().getId();

        // 요청에 담긴 optionId와 qty를 서비스에 전달
        inventoryService.setQty(ownerId, req.optionId(), req.qty());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/products/bulk-delete")
    @Operation(summary = "선택 상품 삭제")
    @Transactional
    public ResponseEntity<?> bulkDelete(
            @RequestBody Map<String, List<?>> body,
            @AuthenticationPrincipal CustomUserDetails me
    ) {
        var raw = body.get("productIds");
        if (raw == null || raw.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "삭제할 상품이 없습니다."));
        }

        var productIds = raw.stream()
                .map(v -> (v instanceof Number n) ? n.longValue() : Long.parseLong(String.valueOf(v)))
                .distinct()
                .toList();

        int deleted = sellerProductService.deleteOwnedProducts(me.getMember().getId(), productIds);
        return ResponseEntity.ok(Map.of("success", true, "deleted", deleted));
    }
}
