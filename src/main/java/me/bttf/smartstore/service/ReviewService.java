package me.bttf.smartstore.service;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.domain.member.Member;
import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.review.Review;
import me.bttf.smartstore.dto.review.ReviewCreateReq;
import me.bttf.smartstore.dto.review.ReviewRes;
import me.bttf.smartstore.dto.review.ReviewUpdateReq;
import me.bttf.smartstore.exception.ResourceNotFoundException;
import me.bttf.smartstore.repository.MemberRepository;
import me.bttf.smartstore.repository.ProductRepository;
import me.bttf.smartstore.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ProductRepository productRepo;
    private final MemberRepository memberRepo;
    private final ReviewRepository reviewRepo;

    public ReviewRes write(Long productId, ReviewCreateReq req) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("상품이 없습니다. id=" + productId));
        Member member = memberRepo.findById(req.memberId())
                .orElseThrow(() -> new ResourceNotFoundException("회원이 없습니다. id=" + req.memberId()));

        if (reviewRepo.existsByProduct_IdAndMember_Id(productId, req.memberId())) {
            throw new IllegalStateException("이미 이 상품에 리뷰를 작성했습니다.");
        }

        Review review = Review.create(product, member, req.rating(), req.content());
        Review saved = reviewRepo.save(review);
        return toRes(saved);
    }

    public ReviewRes update(Long reviewId, ReviewUpdateReq req) {
        Review r = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("리뷰가 없습니다. id=" + reviewId));

        if (req.rating() != null) r.changeRating(req.rating());
        if (req.content() != null) r.changeContent(req.content());

        return toRes(r);
    }

    public void delete(Long reviewId) {
        Review r = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("리뷰가 없습니다. id=" + reviewId));
        reviewRepo.delete(r);
    }

    @Transactional(readOnly = true)
    public Page<ReviewRes> listByProduct(Long productId, Pageable pageable) {
        return reviewRepo.findByProduct_Id(productId, pageable).map(this::toRes);
    }

    @Transactional(readOnly = true)
    public ReviewStats getStats(Long productId) {
        double avg  = reviewRepo.avgRatingByProductId(productId);
        long count  = reviewRepo.countByProduct_Id(productId);
        return new ReviewStats(avg, count);
    }

    public record ReviewStats(double avg, long count) {}

    private ReviewRes toRes(Review r) {
        return new ReviewRes(
                r.getId(),
                r.getProduct().getId(),
                r.getMember().getId(),
                r.getRating(),
                r.getContent(),
                r.getCreatedAt()
        );
    }
}
