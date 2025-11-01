package me.bttf.smartstore.controller.page.user;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.domain.product.ProductStatus;
import me.bttf.smartstore.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// 1. 메인 페이지 (/)
@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final ProductRepository productRepo;

    @GetMapping("/")
    public String home(Model model) {
        var pageable = PageRequest.of(0, 8);

        var specials = productRepo.findTopCards(ProductStatus.ACTIVE, pageable);
        var bests    = productRepo.findTopBest(ProductStatus.ACTIVE, pageable);
        var news     = productRepo.findTopCards(ProductStatus.ACTIVE, pageable);

        model.addAttribute("specialProducts", specials);
        model.addAttribute("bestProducts", bests);
        model.addAttribute("newProducts", news);
        return "index";
    }
}
