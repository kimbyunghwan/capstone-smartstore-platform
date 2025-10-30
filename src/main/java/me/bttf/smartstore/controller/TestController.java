package me.bttf.smartstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class TestController {

    @GetMapping("/register")
    public String registerPage() {
        return "register";  // templates/register.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // 나중에 만들 로그인 페이지
    }

    @GetMapping("/products")
    public String productList(Model model) {
        return "products/list";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        return "products/detail";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        return "cart";
    }

    @GetMapping("/order/create")
    public String orderCreate(Model model) {
        return "order/create";
    }

    @GetMapping("/orders")
    public String orderList(Model model, @RequestParam(defaultValue = "1") int page) {
        return "order/list";
    }

    @GetMapping("/reviews/{productId}/new")
    public String reviewCreate(@PathVariable Long productId, Model model) {
        return "review/create";
    }

    @PostMapping("/reviews/create")
    public String createReview(@RequestParam Long productId,
                               @RequestParam int rating,
                               @RequestParam String content,
                               @RequestParam(required = false) MultipartFile[] photos) {
        return "redirect:/products/" + productId;
    }

    @GetMapping("/seller/dashboard")
    public String sellerDashboard(Model model) {
        return "seller/dashboard";
    }
}