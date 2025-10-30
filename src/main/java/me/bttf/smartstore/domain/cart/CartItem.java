<<<<<<< HEAD
package me.bttf.smartstore.domain.cart;

public class CartItem {
=======
package me.bttf.smartstore.cart;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.product.ProductOption;

@Entity
@Getter
public class CartItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductOption option;
    private Integer qty;
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
