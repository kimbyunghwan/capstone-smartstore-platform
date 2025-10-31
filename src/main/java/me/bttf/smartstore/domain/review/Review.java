package me.bttf.smartstore.domain.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.member.Member;
import me.bttf.smartstore.domain.product.Product;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "review",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_review_product_member", columnNames = {"product_id", "member_id"})
        },
        indexes = {
                @Index(name = "ix_review_product", columnList = "product_id"),
                @Index(name = "ix_review_member", columnList = "member_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "review_id"))
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer rating; // 1~5

    @Lob
    @Column(nullable = false)
    private String content;

    @Builder(access = AccessLevel.PRIVATE)
    private Review(Product product, Member member, Integer rating, String content) {
        this.product = Objects.requireNonNull(product, "product는 필수");
        this.member  = Objects.requireNonNull(member, "member는 필수");
        changeRating(rating);
        changeContent(content);
    }

    /** 정적 팩토리: 서비스에서 Review.create(...)로 사용 */
    public static Review create(Product product, Member member, Integer rating, String content) {
        return Review.builder()
                .product(product)
                .member(member)
                .rating(rating)
                .content(content)
                .build();
    }

    /** 평점 변경 (1~5 검증) */
    public void changeRating(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("평점은 1~5 범위여야 합니다.");
        }
        this.rating = rating;
    }

    /** 내용 변경 (공백 금지) */
    public void changeContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("리뷰 내용은 필수입니다.");
        }
        this.content = content;
    }
}
