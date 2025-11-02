package me.bttf.smartstore.controller.page.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedPageController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // templates/access-denied.html
    }
}
