package me.bttf.smartstore.controller.page.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 2~3. 회원가입(/register), 로그인(/login)
@Controller
public class AuthPageController {

    @GetMapping("/register")
    public String register() {
        return "register"; // templates/register.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }
}