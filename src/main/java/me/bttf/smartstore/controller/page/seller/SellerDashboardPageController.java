package me.bttf.smartstore.controller.page.seller;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.auth.CustomUserDetails;
import me.bttf.smartstore.dto.inventory.InventoryRowRes;
import me.bttf.smartstore.dto.product.ProductCreateForm;
import me.bttf.smartstore.dto.product.ProductUpdateForm;
import me.bttf.smartstore.repository.ProductRepository;
import me.bttf.smartstore.repository.StoreRepository;
import me.bttf.smartstore.service.CategoryService;
import me.bttf.smartstore.service.SellerProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SELLER')")
public class SellerDashboardPageController {

    private final SellerProductService sellerProductService;
    private final ProductRepository productRepo;
    private final StoreRepository storeRepo;
    private final CategoryService categoryService;

    // 10. 내 상품 관리
    @GetMapping("/dashboard")
    public String dashboard() {
        return "seller/dashboard"; // templates/seller/dashboard.html
    }

    // 11. 상품 등록
    @GetMapping("/products/new")
    public String newProduct(Model model) {
        model.addAttribute("form", new ProductCreateForm());
        model.addAttribute("categories", categoryService.findDepth1());
        return "seller/product-new";
    }

    // 상품 등록 처리 (폼 제출)
    @PostMapping("/products/create")
    public String createProduct(@ModelAttribute ProductCreateForm form,
                                @AuthenticationPrincipal CustomUserDetails user,
                                RedirectAttributes ra) {
        Long id = sellerProductService.createFromForm(form, user.getMember().getId());
        ra.addFlashAttribute("msg", "상품이 등록되었습니다!");
        return "seller/dashboard";
    }

    // 12. 상품 수정
    @GetMapping("/products/{id}/edit")
    public String editProduct(@PathVariable Long id,
                              @AuthenticationPrincipal CustomUserDetails user,
                              Model model) {

        // 상품 DTO (수정용)
        var p = sellerProductService.getEditView(id, user.getMember().getId());
        model.addAttribute("product", p);

        // 카테고리 목록 (드롭다운용)
        model.addAttribute("categories", categoryService.findDepth1());

        // 대표 카테고리 ID (선택 표시용)
        Long selectedCategoryId = sellerProductService.getPrimaryCategoryId(id);
        model.addAttribute("selectedCategoryId", selectedCategoryId);

        return "seller/product-edit";
    }

    // 상품 수정 처리 (폼 제출)
    @PostMapping("/products/{id}/update")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute ProductUpdateForm form,
                                @AuthenticationPrincipal CustomUserDetails user,
                                RedirectAttributes ra) {
        form.setProductId(id);
        sellerProductService.updateFromForm(form, user.getMember().getId());
        ra.addFlashAttribute("msg", "상품이 수정되었습니다!");
        return "redirect:/seller/inventory";
    }


    // 13. 재고 관리
    @GetMapping("/inventory")
    public String inventory(@AuthenticationPrincipal CustomUserDetails user,
                            @RequestParam(name = "search", required = false) String search,
                            @RequestParam(name = "page", defaultValue = "1") int page,
                            Model model) {

        int size = 10;
        var pageable = PageRequest.of(Math.max(page - 1, 0), size);

        Long memberId = user.getMember().getId();
        Long storeId = storeRepo.findIdByOwner_Id(memberId)
                .orElseThrow(() -> new IllegalStateException("해당 회원의 스토어가 없습니다."));

        Page<InventoryRowRes> result =
                productRepo.findInventory(storeId, search, pageable);

        model.addAttribute("products", result.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", Math.max(result.getTotalPages(), 1));
        model.addAttribute("totalProducts", result.getTotalElements());

        long outOf = Optional.ofNullable(productRepo.countOutOfStock(storeId)).orElse(0L);
        long low   = Optional.ofNullable(productRepo.countLowStock(storeId)).orElse(0L);
        long in    = result.getTotalElements() - outOf - low;

        model.addAttribute("inStockProducts", in);
        model.addAttribute("lowStockProducts", low);
        model.addAttribute("outOfStockProducts", outOf);

        return "seller/inventory";
    }
}
