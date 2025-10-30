<<<<<<< HEAD
package me.bttf.smartstore.domain.store;

public class Store {
=======
package me.bttf.smartstore.store;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import me.bttf.smartstore.member.Member;
import me.bttf.smartstore.product.ProductStatus;

public class Store {
    private String storeName;
    private String intro;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member owner;
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
