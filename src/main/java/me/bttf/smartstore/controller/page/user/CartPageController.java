package me.bttf.smartstore.controller.page.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 6. 장바구니(/cart)
@Controller
public class CartPageController {

    @GetMapping("/cart")
    public String cart() {
        return "cart"; // templates/cart.html
    }
}