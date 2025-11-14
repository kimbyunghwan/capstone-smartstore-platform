package me.bttf.smartstore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @Value("${bootpay.app-id}")
    private String bootpayAppId;

    @ModelAttribute("bootpayAppId")
    public String bootpayAppId() { return bootpayAppId; }
}
