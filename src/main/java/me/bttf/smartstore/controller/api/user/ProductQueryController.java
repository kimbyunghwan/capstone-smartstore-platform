package me.bttf.smartstore.controller.api.user;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.exception.ResourceNotFoundException;
import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.dto.product.ProductDetailRes;
import me.bttf.smartstore.dto.product.ProductListRes;
import me.bttf.smartstore.repository.ProductRepository;
import me.bttf.smartstore.service.ProductCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Transactional(readOnly = true)
public class ProductQueryController {

    private final ProductCategoryService productCategoryService;
    private final ProductRepository productRepo;

    // 4. 상품 목록 (/products?q=&categoryId=&includeChildren=true&page=&size=)
    @GetMapping
    @Operation(summary = "상품 목록")
    public Page<ProductListRes> list(
            @RequestParam(required=false) String q,
            @RequestParam(required=false) Long categoryId,
            @RequestParam(defaultValue="true") boolean includeChildren,
            @PageableDefault(size=12, sort="id", direction= Sort.Direction.DESC) Pageable pageable) {

        Page<Product> page = (categoryId != null)
                ? productCategoryService.listByCategory(categoryId, includeChildren, pageable)
                : (q == null || q.isBlank()
                ? productRepo.findAll(pageable)
                : productRepo.findByNameContainingIgnoreCase(q, pageable));

        return page.map(ProductListRes::from);
    }

    // 5. 상품 상세 (/products/{id})
    @GetMapping("/{id}")
    @Operation(summary = "상품 상세")
    public ProductDetailRes detail(@PathVariable Long id) {
        Product p = productRepo.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다. id=" + id));
        return ProductDetailRes.from(p);
    }
}
