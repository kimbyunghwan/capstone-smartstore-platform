package me.bttf.smartstore.domain.inventory;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.product.ProductOption;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "inventory_tx",
        indexes = {
                @Index(name = "ix_inventory_option_created", columnList = "option_id, created_at")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "tx_id"))
public class InventoryTx extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOption option;

    @Enumerated(EnumType.STRING)
    @Column(name = "tx_type", nullable = false, length = 10)
    private TxType txType; // IN/OUT/ADJUST

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "reason", columnDefinition = "text")
    private String reason; // 주문, 반품, 입고, 조정 등

    @Column(name = "related_order_id")
    private Long relatedOrderId; // 필요시 참조 id만 저장

    @Builder
    private InventoryTx(ProductOption option, TxType txType, int qty, String reason, Long relatedOrderId) {
        if (option == null) throw new IllegalArgumentException("option 필수");
        if (txType == null) throw new IllegalArgumentException("txType 필수");
        if (txType != TxType.ADJUST && qty == 0)
            throw new IllegalArgumentException("0 수량은 ADJUST에서만 허용");

        this.option = option;
        this.txType = txType;
        this.qty = qty;
        this.reason = reason;
        this.relatedOrderId = relatedOrderId;
    }
}
