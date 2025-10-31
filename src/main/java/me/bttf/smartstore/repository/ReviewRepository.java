package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProduct_Id(Long productId, Pageable pageable);

    boolean existsByProduct_IdAndMember_Id(Long productId, Long memberId);
}
