package me.bttf.smartstore.controller.page.seller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
public class SellerDashboardPageController {

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

    // 12. 상품 수정
    @GetMapping("/products/{id}/edit")
    public String editProduct(@PathVariable Long id) {
        return "seller/product-edit"; // templates/seller/product-edit.html
    }

    // 13. 재고 관리
    @GetMapping("/inventory")
    public String inventory() {
        return "seller/inventory"; // templates/seller/inventory.html
    }
}
