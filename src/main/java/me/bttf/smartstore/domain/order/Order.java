package me.bttf.smartstore.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.common.Money;
import me.bttf.smartstore.domain.member.Address;
import me.bttf.smartstore.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
