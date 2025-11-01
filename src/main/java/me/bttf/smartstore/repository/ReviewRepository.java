package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProduct_Id(Long productId, Pageable pageable);

    boolean existsByProduct_IdAndMember_Id(Long productId, Long memberId);

    @Query("select coalesce(avg(r.rating), 0) from Review r where r.product.id = :productId")
    double avgRatingByProductId(Long productId);

    long countByProduct_Id(Long productId);
}
