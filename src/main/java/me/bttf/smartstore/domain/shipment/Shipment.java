package me.bttf.smartstore.domain.shipment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "shipment",
        indexes = {
                @Index(name = "ix_shipment_order", columnList = "order_id"),
                @Index(name = "ix_shipment_tracking_no", columnList = "tracking_no")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "shipment_id"))
public class Shipment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "carrier", length = 50)
    private String carrier;

    @Column(name = "tracking_no", length = 100)
    private String trackingNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ShipmentStatus status;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipmentItem> items = new ArrayList<>();

    public void changeStatus(ShipmentStatus next) {
        if (this.status == next) return;
        // READY->SHIPPED->DELIVERED/RETURNED
        switch (this.status) {
            case READY -> { if (next != ShipmentStatus.SHIPPED) throw new IllegalStateException("READY→SHIPPED만 허용"); }
            case SHIPPED -> {
                if (next == ShipmentStatus.DELIVERED) this.deliveredAt = LocalDateTime.now();
                else if (next != ShipmentStatus.RETURNED) throw new IllegalStateException("SHIPPED→DELIVERED/RETURNED만 허용");
            }
            default -> throw new IllegalStateException("종료 상태 변경 불가");
        }
        if (next == ShipmentStatus.SHIPPED && this.shippedAt == null) this.shippedAt = LocalDateTime.now();
        this.status = next;
    }
}
