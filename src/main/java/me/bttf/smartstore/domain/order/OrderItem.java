<<<<<<< HEAD
package me.bttf.smartstore.domain.order;

public class OrderItem {
=======
package me.bttf.smartstore.order;

import jakarta.persistence.*;
import lombok.Getter;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.common.Money;
import me.bttf.smartstore.product.Product;
import me.bttf.smartstore.product.ProductOption;

@Entity
@Getter
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductOption option;

    // 스냅샷
    private String productName;
    private String optionName;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column=@Column(name="unit_price"))
    })
    private Money unitPrice;

    private Integer qty;
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
