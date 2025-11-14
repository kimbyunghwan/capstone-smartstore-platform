package me.bttf.smartstore.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.common.Money;
import me.bttf.smartstore.domain.member.Address;
import me.bttf.smartstore.domain.member.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "orders")
@AttributeOverride(name = "id", column = @Column(name = "order_id"))
public class Order extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recv_address_id")
    private Address recvAddressRef; // null 허용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "order_code", nullable = false, unique = true, length = 40)
    private String orderCode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name",    column = @Column(name = "recv_name",  length = 60,  nullable = false)),
            @AttributeOverride(name = "phone",   column = @Column(name = "recv_phone", length = 30,  nullable = false)),
            @AttributeOverride(name = "addr1",   column = @Column(name = "recv_addr1", length = 200, nullable = false)),
            @AttributeOverride(name = "addr2",   column = @Column(name = "recv_addr2", length = 200)),
            @AttributeOverride(name = "zipcode", column = @Column(name = "recv_zip",   length = 10,  nullable = false))
    })
    private AddressSnapshot recvAddress;

    // 주문 총액 DECIMAL(14,2)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount",
                    column = @Column(name = "order_total", precision = 14, scale = 2, nullable = false))
    })
    private Money orderTotal;


    @Column(name = "recv_memo", length = 255)
    private String recvMemo;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order(Member member, AddressSnapshot recvAddress, String recvMemo) {
        this.member = Objects.requireNonNull(member);
        this.recvAddress = Objects.requireNonNull(recvAddress);
        this.recvMemo = recvMemo;
        this.status = OrderStatus.PENDING;
        this.orderTotal = Money.of(java.math.BigDecimal.ZERO);
        this.orderCode = "ORD-" + System.currentTimeMillis();
    }



    public void addItem(OrderItem item) {
        items.add(item);

        if (item.getOrder() != this) {
            try {
                var orderField = OrderItem.class.getDeclaredField("order");
                orderField.setAccessible(true);
                orderField.set(item, this);
            } catch (Exception ignore) {}
        }
    }

    public void setTotal(Money total) { this.orderTotal = total; }

    public void changeStatus(OrderStatus status) { this.status = status; }
}
