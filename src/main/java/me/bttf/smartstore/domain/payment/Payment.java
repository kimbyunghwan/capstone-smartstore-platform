package me.bttf.smartstore.domain.payment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.common.Money;
import me.bttf.smartstore.domain.order.Order;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "payment",
        indexes = { @Index(name = "ix_pg_transaction", columnList = "pg_tid") }
)
@AttributeOverride(name = "id", column = @Column(name = "payment_id"))
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount",
                    column = @Column(name = "amount", precision = 14, scale = 2, nullable = false))
    })
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    // 결제 완료(승인) 시각
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    // PG사 거래 번호(= ERD의 pg_tid)
    @Column(name = "pg_tid", length = 100, unique = true)
    private String pgTid;

}
