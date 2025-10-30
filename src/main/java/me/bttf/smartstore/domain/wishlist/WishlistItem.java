package me.bttf.smartstore.domain.wishlist;

import jakarta.persistence.*;
import lombok.Getter;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.product.Product;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "wishlist_item",
        uniqueConstraints = @UniqueConstraint(name="uk_wishlist_product", columnNames={"wishlist_id","product_id"}),
        indexes = {
                @Index(name="ix_wishlist_item_wishlist", columnList="wishlist_id"),
                @Index(name="ix_wishlist_item_product", columnList="product_id")
        }
)
public class WishlistItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "added_at", nullable = false)
    private LocalDateTime addedAt = LocalDateTime.now();

    protected WishlistItem() {} // JPA용

    public WishlistItem(Wishlist wishlist, Product product) {
        this.wishlist = wishlist;
        this.product  = product;
    }

    void setWishlist(Wishlist wishlist) { this.wishlist = wishlist; }
}
