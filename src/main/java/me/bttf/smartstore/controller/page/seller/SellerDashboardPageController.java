package me.bttf.smartstore.controller.page.seller;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.dto.product.ProductCreateForm;
import me.bttf.smartstore.dto.product.ProductUpdateForm;
import me.bttf.smartstore.service.SellerProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerDashboardPageController {

    private final SellerProductService sellerProductService;

    // 10. 내 상품 관리
    @GetMapping("/dashboard")
    public String dashboard() {
        return "seller/dashboard"; // templates/seller/dashboard.html
    }

    // 11. 상품 등록
    @GetMapping("/products/new")
    public String newProduct() {
        return "seller/product-new"; // templates/seller/product-new.html
    }

    // 상품 등록 처리 (폼 제출)
    @PostMapping("/products/create")
    public String createProduct(@ModelAttribute ProductCreateForm form,
                                RedirectAttributes ra) {
        Long id = sellerProductService.createFromForm(form);
        ra.addFlashAttribute("msg", "상품이 등록되었습니다!");
        return "redirect:/seller/products/" + id + "/edit";
    }

    // 12. 상품 수정
    @GetMapping("/products/{id}/edit")
    public String editProduct(@PathVariable Long id) {
        return "seller/product-edit"; // templates/seller/product-edit.html
    }

    // 상품 수정 처리 (폼 제출)
    @PostMapping("/products/{id}/update")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute ProductUpdateForm form,
                                RedirectAttributes ra) {
        form.setProductId(id);
        sellerProductService.updateFromForm(form);
        ra.addFlashAttribute("msg", "상품이 수정되었습니다!");
        return "redirect:/seller/products/" + id + "/edit";
    }

    // 13. 재고 관리
    @GetMapping("/inventory")
    public String inventory() {
        return "seller/inventory"; // templates/seller/inventory.html
    }
}
