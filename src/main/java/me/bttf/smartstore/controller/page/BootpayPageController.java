package me.bttf.smartstore.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pay/bootpay")
public class BootpayPageController {

    @GetMapping("/redirect")
    public String bootpayRedirect(
            @RequestParam(name="receipt_id", required=false) String receiptId,
            @RequestParam(name="order_id", required=false) String orderId,
            Model model) {

        model.addAttribute("receiptId", receiptId);
        model.addAttribute("orderId", orderId);
        return "pay/bootpay-redirect"; // templates/pay/bootpay-redirect.html
    }
}
