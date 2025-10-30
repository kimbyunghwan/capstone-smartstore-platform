<<<<<<< HEAD
package me.bttf.smartstore.domain.order;

public class Order {
=======
package me.bttf.smartstore.order;

import jakarta.persistence.*;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Embedded
    private AddressSnapshot recvAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
