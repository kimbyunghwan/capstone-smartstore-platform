<<<<<<< HEAD
package me.bttf.smartstore.domain.wishlist;

public class WishlistItem {
=======
package me.bttf.smartstore.wishlist;

import jakarta.persistence.*;
import lombok.Getter;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class WishlistItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> items = new ArrayList<>();
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
