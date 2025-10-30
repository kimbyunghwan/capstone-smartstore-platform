package me.bttf.smartstore.domain.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "cart",
        indexes = {
                @Index(name = "ix_cart_member", columnList = "member_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "cart_id"))
public class Cart extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Builder
    private Cart(Member member) {
        if (member == null) throw new IllegalArgumentException("member는 필수");
        this.member = member;
    }
}
