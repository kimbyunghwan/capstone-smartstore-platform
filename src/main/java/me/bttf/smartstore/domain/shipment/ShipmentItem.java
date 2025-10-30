<<<<<<< HEAD
package me.bttf.smartstore.domain.shipment;

public class ShipmentItem {
}
=======
package me.bttf.smartstore.shipment;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.order.OrderItem;

public class ShipmentItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.LAZY) private OrderItem orderItem;
    private Integer qty;
}
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
