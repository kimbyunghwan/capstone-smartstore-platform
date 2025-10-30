<<<<<<< HEAD
package me.bttf.smartstore.domain.payment;

public class Payment {
=======
package me.bttf.smartstore.payment;

import jakarta.persistence.*;
import lombok.Getter;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.order.Order;

@Entity
@Getter
public class Payment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String pgTransactionId;
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
