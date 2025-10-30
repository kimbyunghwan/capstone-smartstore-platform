<<<<<<< HEAD
package me.bttf.smartstore.domain.shipment;

public class Shipment {
=======
package me.bttf.smartstore.shipment;

import jakarta.persistence.*;
import lombok.Getter;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Shipment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;
    private String courier;
    private String trackingNo;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipmentItem> items = new ArrayList<>();
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
