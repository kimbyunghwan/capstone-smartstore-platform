package me.bttf.smartstore.controller.page.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 9. 리뷰 작성(/reviews/{productId}/new)
@Controller
public class ReviewPageController {

    @GetMapping("/reviews/{productId}/new")
    public String newReview(@PathVariable Long productId) {
        return "review/create"; // templates/review/create.html
    }
}
