package me.bttf.smartstore.controller.page.user;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.dto.product.ProductDetailRes;
import me.bttf.smartstore.exception.ResourceNotFoundException;
import me.bttf.smartstore.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// 4~5. 상품 목록(/products), 상품 상세(/products/{id})
@Controller
@RequiredArgsConstructor
public class ProductPageController {

    private final ProductRepository productRepo;

    @GetMapping("/products")
    public String list() {
        return "products/list"; // templates/products/list.html
    }

    @GetMapping("/products/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product p = productRepo.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다. id=" + id));

        ProductDetailRes dto = ProductDetailRes.from(p);
        model.addAttribute("product", dto);
        model.addAttribute("reviews", List.of());
        return "products/detail";
    }
}

