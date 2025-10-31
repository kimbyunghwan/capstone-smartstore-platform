package me.bttf.smartstore.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.domain.category.Category;
import me.bttf.smartstore.dto.category.CategoryReq;
import me.bttf.smartstore.dto.category.CategoryRes;
import me.bttf.smartstore.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/categories")
public class CategoryController {

    private final CategoryRepository categoryRepo;

    @Operation(summary = "카테고리 생성")
    @PostMapping
    @Transactional
    public CategoryRes create(@RequestBody CategoryReq req) {
        Category parent = (req.parentId() == null) ? null
                : categoryRepo.findById(req.parentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent not found: " + req.parentId()));

        int depth = (req.depth() != null) ? req.depth()
                : (parent == null ? 1 : parent.getDepth() + 1);

        if (depth < 1 || depth > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "depth must be 1..3");
        }
        if (depth == 1 && parent != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "depth=1 must have no parent");
        }
        if (depth > 1 && parent == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "depth>1 requires parentId");
        }
        if (parent != null && depth != parent.getDepth() + 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "depth must be parent.depth + 1");
        }

        Category c = new Category(req.name(), depth, parent);
        categoryRepo.save(c);
        return new CategoryRes(c.getId(), c.getName(), c.getDepth(), parent == null ? null : parent.getId());
    }
}