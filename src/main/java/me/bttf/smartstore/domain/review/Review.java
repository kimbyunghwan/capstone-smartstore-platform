package me.bttf.smartstore.domain.review;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.member.Member;
import me.bttf.smartstore.domain.product.Product;

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
    private String content;
}
