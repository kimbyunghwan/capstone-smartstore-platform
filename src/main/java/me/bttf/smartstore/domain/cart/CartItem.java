package me.bttf.smartstore.domain.cart;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.product.ProductOption;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "cart_item",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cart_option",
                        columnNames = {"cart_id", "option_id"}
                )
        },
        indexes = {
                @Index(name = "ix_cart_item_cart", columnList = "cart_id"),
                @Index(name = "ix_cart_item_option", columnList = "option_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "cart_item_id"))
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOption option;

    @Min(1)
    @Column(nullable = false)
    private Integer qty;
}
