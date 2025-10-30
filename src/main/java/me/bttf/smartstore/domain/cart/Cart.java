<<<<<<< HEAD
package me.bttf.smartstore.domain.cart;

public class Cart {
=======
package me.bttf.smartstore.cart;

import jakarta.persistence.*;
import lombok.Getter;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Cart extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
