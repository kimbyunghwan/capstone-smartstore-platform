package me.bttf.smartstore.service;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.domain.category.Category;
import me.bttf.smartstore.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /** 1뎁스 목록 (폼 드롭다운용) */
    public List<Category> findDepth1() {
        return categoryRepository.findByDepth(1);
    }

    /** 특정 부모의 자식 카테고리 */
    public List<Category> findChildren(Long parentId) {
        return categoryRepository.findByParent_Id(parentId);
    }

}