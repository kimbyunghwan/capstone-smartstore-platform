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

    @GetMapping("/seller/product-new")
    public String productNew() {
        return "seller/product-new";
    }

    @PostMapping("/seller/products/create")
    public String createProduct(@RequestParam String productName,
                                @RequestParam String category,
                                @RequestParam int originalPrice,
                                @RequestParam int salePrice,
                                @RequestParam int stockQty,
                                @RequestParam(required = false) MultipartFile mainImage,
                                @RequestParam(required = false) MultipartFile[] detailImages) {
        // 상품 저장 로직
        // productService.create(...);

        return "redirect:/seller/inventory";
    }

    @GetMapping("/seller/inventory")
    public String inventory(Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(required = false) String search) {
        // 실제 데이터 연동 시
        // Page<Product> productPage = productService.getSellerProducts(sellerId, page, search);
        // model.addAttribute("products", productPage.getContent());
        // model.addAttribute("totalPages", productPage.getTotalPages());
        // model.addAttribute("currentPage", page);
        // model.addAttribute("totalProducts", productService.getSellerProductCount());
        // model.addAttribute("inStockProducts", productService.getInStockCount());
        // model.addAttribute("lowStockProducts", productService.getLowStockCount());
        // model.addAttribute("outOfStockProducts", productService.getOutOfStockCount());

        return "seller/inventory";
    }

    @GetMapping("/seller/products/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        // 실제 데이터 연동 시
        // Product product = productService.getById(id);
        // model.addAttribute("product", product);

        return "seller/product-edit";
    }

    @PostMapping("/seller/products/{id}/update")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String productName,
                                @RequestParam String category,
                                @RequestParam int originalPrice,
                                @RequestParam int salePrice,
                                @RequestParam int stockQty,
                                @RequestParam(required = false) MultipartFile mainImage,
                                @RequestParam(required = false) MultipartFile[] detailImages) {
        // 상품 업데이트 로직
        // productService.update(id, ...);

        return "redirect:/seller/inventory";
    }

    @PostMapping("/seller/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        // 상품 삭제 로직
        // productService.delete(id);

        return "redirect:/seller/inventory";
    }
}