package me.bttf.smartstore.controller.api.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.dto.review.ReviewCreateReq;
import me.bttf.smartstore.dto.review.ReviewRes;
import me.bttf.smartstore.dto.review.ReviewUpdateReq;
import me.bttf.smartstore.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
@Transactional(readOnly = true)
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성 (/reviews/{productId}/new)
    @PostMapping("/{productId}/new")
    @Transactional
    public ReviewRes write(@PathVariable Long productId,
                           @RequestBody @Valid ReviewCreateReq req) {
        return reviewService.write(productId, req);
    }

    // 리뷰 수정 (/reviews/{reviewId})
    @PatchMapping("/{reviewId}")
    @Transactional
    public ReviewRes edit(@PathVariable Long reviewId,
                          @RequestBody @Valid ReviewUpdateReq req) {
        return reviewService.update(reviewId, req);
    }

    // 리뷰 삭제 (/reviews/{reviewId})
    @DeleteMapping("/{reviewId}")
    @Transactional
    public void delete(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
    }

    // 특정 상품의 리뷰 목록 (/reviews?productId=1&page=0&size=10)
    @GetMapping
    public Page<ReviewRes> list(@RequestParam Long productId,
                                @PageableDefault(size = 10) Pageable pageable) {
        return reviewService.listByProduct(productId, pageable);
    }
}