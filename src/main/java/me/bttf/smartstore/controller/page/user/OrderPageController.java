package me.bttf.smartstore.controller.page.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 7~8. 주문 생성(/order), 주문 내역(/orders)
@Controller
public class OrderPageController {

    @GetMapping("/order")
    public String createOrder() {
        return "order/create"; // templates/order/create.html
    }

    @GetMapping("/orders")
    public String orderList() {
        return "order/list"; // templates/order/list.html
    }
}
