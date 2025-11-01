package me.bttf.smartstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.bttf.smartstore.domain.common.Money;
import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductOption;
import me.bttf.smartstore.domain.product.ProductStatus;
import me.bttf.smartstore.domain.store.Store;
import me.bttf.smartstore.dto.product.ProductCreateForm;
import me.bttf.smartstore.dto.product.ProductUpdateForm;
import me.bttf.smartstore.dto.seller.ProductCreateReq;
import me.bttf.smartstore.dto.seller.ProductUpdateReq;
import me.bttf.smartstore.dto.seller.SkuReq;
import me.bttf.smartstore.repository.ProductOptionRepository;
import me.bttf.smartstore.repository.ProductRepository;
import me.bttf.smartstore.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SellerProductService {

    private final ProductRepository productRepo;
    private final ProductOptionRepository optionRepo;
    private final ProductCategoryService categoryService;
    private final StoreRepository storeRepo;
    private final ImageStorage imageStorage;

    /* ===================== 1) 폼 기반 ===================== */

    public Long createFromForm(ProductCreateForm form) {
        // 0) 스토어 결정 (예: 로그인 판매자의 storeId). 폼에 없으면 인증정보로 조회해 바인딩.
        Store store = resolveCurrentSellerStore();

        // 1) Product 생성 (이름/상태만)
        Product p = Product.builder()
                .store(store)
                .name(form.getProductName())
                .status(Boolean.TRUE.equals(form.getIsActive())
                        ? ProductStatus.ACTIVE
                        : ProductStatus.INACTIVE)
                .build();
        productRepo.save(p);

        p.changeDescriptions(form.getShortDescription(), form.getDescription());

        // 2) 이미지 업로드 (Product에 이미지 필드가 없으면, 별도 Image 엔티티/테이블 사용하거나 옵션 상세에만 사용)
        String mainUrl = imageStorage.store(form.getMainImage());
        log.info("thumbnail url={}", mainUrl);
        p.changeThumbnail(mainUrl);
        productRepo.save(p);

        // 3) 카테고리 매핑 (문자열 category → id 매핑 로직이 있다면 여기서)
        // TODO: categoryService.assignByCode(p.getId(), form.getCategory(), true);

        // 4) 옵션 생성 + 초기 재고/가격
        createOptionsFromForm(p, form, mainUrl);

        return p.getId();
    }

    public void updateFromForm(ProductUpdateForm form) {
        Product p = productRepo.findById(form.getProductId()).orElseThrow();

        // 이름만 변경 (설명/이미지 등을 Product에 둘 계획이면 도메인 메서드 추가)
        p.changeName(form.getProductName());

        // 이미지 교체 저장은 위와 동일하게 별도 저장처가 있을 때만 수행
        if (form.getMainImage() != null && !form.getMainImage().isEmpty()) {
            String mainUrl = imageStorage.store(form.getMainImage());

        }
        if (form.getDetailImages() != null && !form.getDetailImages().isEmpty()) {
            List<String> detailUrls = imageStorage.storeAll(form.getDetailImages());

        }

        var options = optionRepo.findByProduct_Id(p.getId());
        Money newPrice = Money.of(form.getSalePrice().longValue());
        int newStock = form.getStockQty();
        for (var opt : options) {
            opt.changePrice(newPrice);
            opt.changeStock(newStock);
        }

        // 판매 상태 토글
        if (Boolean.TRUE.equals(form.getIsActive())) p.changeStatus(ProductStatus.ACTIVE);
        else p.changeStatus(ProductStatus.INACTIVE);
    }

    private void createOptionsFromForm(Product p, ProductCreateForm form, String mainUrl) {
        List<String> v1 = split(form.getOption1Values());
        List<String> v2 = split(form.getOption2Values());

        Money price = Money.of(form.getSalePrice().longValue());
        int stock = form.getStockQty();

        // 옵션이 없으면 기본 SKU 1개
        if (v1.isEmpty() && v2.isEmpty()) {
            optionRepo.save(ProductOption.builder()
                    .product(p)
                    .sku(genSku(p.getId(), 1))
                    .optionName("기본")
                    .attrJson(null)
                    .price(price)
                    .stockQty(stock)
                    .build());
            return;
        }

        int seq = 1;
        for (String a : v1.isEmpty() ? List.of("") : v1) {
            for (String b : v2.isEmpty() ? List.of("") : v2) {
                optionRepo.save(ProductOption.builder()
                        .product(p)
                        .sku(genSku(p.getId(), seq++))
                        .optionName(combine(a, b))              // "빨강 / M"
                        .attrJson(toAttrJson(form.getOption1Name(), a, form.getOption2Name(), b))
                        .price(price)
                        .stockQty(stock)
                        .build());
            }
        }
    }

    /* ===================== 2) API(JSON) ===================== */
    public Long createFromApi(ProductCreateReq req) {
        Store store = storeRepo.findById(req.storeId()).orElseThrow();

        Product p = Product.builder()
                .store(store)
                .name(req.name())
                .status(req.status() != null
                        ? req.status()
                        : ProductStatus.ACTIVE) // 값이 없으면 기본 ACTIVE
                .build();
        productRepo.save(p);

        p.changeDescriptions(req.shortDescription(), req.description());

        categoryService.assign(p.getId(), req.categoryId(), req.primary());

        if (req.skus() != null) {
            int seq = 1;
            for (SkuReq s : req.skus()) {
                optionRepo.save(ProductOption.builder()
                        .product(p)
                        .sku(genSku(p.getId(), seq++))
                        .optionName(s.optionName())
                        .attrJson(null)
                        .price(Money.of(s.price().longValue()))
                        .stockQty(s.stock())
                        .build());
            }
        }
        return p.getId();
    }

    public void updateFromApi(Long id, ProductUpdateReq req) {
        Product p = productRepo.findById(id).orElseThrow();
        if (req.name() != null && !req.name().isBlank()) p.changeName(req.name());
        if (req.status() != null) p.changeStatus(req.status());
    }

    /* ===================== 3) 공통 ===================== */

    @Transactional(readOnly = true)
    public Product getEditView(Long id) {
        return productRepo.findById(id).orElseThrow();
    }

    public void delete(Long id) { productRepo.deleteById(id); }

    /* util */

    private Store resolveCurrentSellerStore() {
        // 임시: 단일 스토어 환경이라면 첫 스토어나 고정 ID 사용
        return storeRepo.findById(1L).orElseThrow(); // 예시
    }

    private static String genSku(Long productId, int seq) {
        return "P%05d-%04d".formatted(productId, seq);
    }

    private static List<String> split(String s) {
        if (s == null || s.isBlank()) return List.of();
        return Arrays.stream(s.split(","))
                .map(String::trim).filter(v -> !v.isBlank()).toList();
    }

    private static String combine(String a, String b) {
        a = a == null ? "" : a.trim();
        b = b == null ? "" : b.trim();
        if (a.isEmpty()) return b;
        if (b.isEmpty()) return a;
        return a + " / " + b;
    }

    private static String toAttrJson(String k1, String v1, String k2, String v2) {
        if ((k1 == null || k1.isBlank()) && (k2 == null || k2.isBlank())) return null;
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        if (k1 != null && !k1.isBlank()) { sb.append('"').append(k1).append("\":\"").append(v1).append('"'); first = false; }
        if (k2 != null && !k2.isBlank()) { if (!first) sb.append(','); sb.append('"').append(k2).append("\":\"").append(v2).append('"'); }
        sb.append('}');
        return sb.toString();
    }
}