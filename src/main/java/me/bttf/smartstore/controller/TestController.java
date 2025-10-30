package me.bttf.smartstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}