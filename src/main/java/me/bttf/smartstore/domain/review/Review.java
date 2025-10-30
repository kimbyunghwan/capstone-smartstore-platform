<<<<<<< HEAD
package me.bttf.smartstore.domain.review;

public class Review {
=======
package me.bttf.smartstore.review;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.member.Member;
import me.bttf.smartstore.product.Product;

@Entity
@Getter
public class Review extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private Integer rating; // 1~5
    @Lob
    private String content;
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
