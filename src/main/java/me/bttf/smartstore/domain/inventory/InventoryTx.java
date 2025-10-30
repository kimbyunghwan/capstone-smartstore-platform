<<<<<<< HEAD
package me.bttf.smartstore.domain.inventory;

public class InventoryTx {
=======
package me.bttf.smartstore.inventory;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.product.ProductOption;

public class InventoryTx extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductOption option;

    @Enumerated(EnumType.STRING)
    private TxType txType; // IN/OUT/ADJUST

    private Integer qty;
    private String reason; // 주문, 반품, 입고, 조정 등
    private Long relatedOrderId; // 필요시 참조 id만 저장
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
