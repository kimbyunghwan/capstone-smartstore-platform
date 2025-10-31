package me.bttf.smartstore.controller.page.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 4~5. 상품 목록(/products), 상품 상세(/products/{id})
@Controller
public class ProductPageController {

    @GetMapping("/products")
    public String list() {
        return "products/list"; // templates/products/list.html
    }

    @GetMapping("/products/{id}")
    public String detail(@PathVariable Long id) {
        return "products/detail"; // templates/products/detail.html
    }
}

