package me.bttf.smartstore.controller.page.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 2~3. 회원가입(/register), 로그인(/login)
@Controller
public class AuthPageController {

    @GetMapping("/register")
    public String register() {
        return "register"; // templates/register.html
    }

    /**
     * 회원가입 처리 (POST)
     */
    @PostMapping("/register")
    public String registerProcess(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String name,
            Model model) {

        // TODO: 회원가입 로직 구현
        // 1. 유효성 검증
        // 2. 비밀번호 암호화
        // 3. DB 저장

        // 임시: 로그인 페이지로 리다이렉트
        return "redirect:/login?registered=true";
    }

    /**
     * 로그인 페이지 (GET)
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String registered,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        if (registered != null) {
            model.addAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.");
        }

        return "login"; // templates/login.html
    }
}