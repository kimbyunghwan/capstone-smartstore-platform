package me.bttf.smartstore.service;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.domain.category.Category;
import me.bttf.smartstore.domain.category.ProductCategory;
import me.bttf.smartstore.domain.category.ProductCategoryId;
import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.repository.CategoryRepository;
import me.bttf.smartstore.repository.ProductCategoryRepository;
import me.bttf.smartstore.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final ProductCategoryRepository pcRepo;

    public void assign(Long productId, Long categoryId, boolean primary) {
        Product p = productRepo.findById(productId).orElseThrow();
        Category c = categoryRepo.findById(categoryId).orElseThrow();

        ProductCategoryId id = new ProductCategoryId(productId, categoryId);
        if (pcRepo.existsById(id)) return;

        if (primary) {
            pcRepo.clearPrimary(productId); // 기존 대표 해제
        }
        pcRepo.save(new ProductCategory(p, c, primary));
    }

    public void setPrimary(Long productId, Long categoryId) {
        ProductCategory pc = pcRepo.findById(new ProductCategoryId(productId, categoryId))
                .orElseThrow();
        pcRepo.clearPrimary(productId);
        pc.setPrimary(true); // setter or 도메인 메서드
        // 변경감지로 flush
    }

    public void unassign(Long productId, Long categoryId) {
        pcRepo.deleteById(new ProductCategoryId(productId, categoryId));
    }

    @Transactional(readOnly = true)
    public Page<Product> listByCategory(Long categoryId, boolean includeChildren, Pageable pageable) {
        List<Long> ids = new ArrayList<>();
        ids.add(categoryId);
        if (includeChildren) {
            ids.addAll(collectChildren(categoryId));
        }
        return pcRepo.findProductsByCategoryIds(ids, pageable);
    }

    // 트리 순회로 하위 id 수집
    private List<Long> collectChildren(Long rootId) {
        List<Long> ids = new ArrayList<>();
        ids.add(rootId);

        // root 바로 아래 자식들
        List<Category> children = categoryRepo.findByParent_Id(rootId);

        // 각 자식에 대해 재귀적으로 하위 탐색
        for (Category child : children) {
            ids.addAll(collectChildren(child.getId()));
        }

        return ids;
    }
}
