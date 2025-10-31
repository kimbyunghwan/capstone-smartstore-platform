package me.bttf.smartstore.controller.page.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 1. 메인 페이지 (/)
@Controller
public class HomePageController {
    @GetMapping("/")
    public String home() {
        return "index"; // templates/index.html
    }
}
