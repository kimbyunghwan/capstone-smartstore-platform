package me.bttf.smartstore.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.common.Money;
import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductOption;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AttributeOverride(name = "id", column = @Column(name = "order_item_id"))
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOption option;

    @Column(nullable = false, length = 200)
    private String productName; // 상품명 스냅샷

    @Column(length = 200)
    private String optionName; // 옵션명 스냅샷

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="amount", column=@Column(name="unit_price", precision = 15, scale = 2, nullable = false))
    })
    private Money unitPrice;

    @Column(nullable = false)
    private Integer qty;

    public OrderItem(Order order, Product product, ProductOption option,
                     String productName, String optionName, Money unitPrice, Integer qty) {
        this.order = Objects.requireNonNull(order);
        this.product = Objects.requireNonNull(product);
        this.option = Objects.requireNonNull(option);
        this.productName = Objects.requireNonNull(productName);
        this.optionName = optionName;
        this.unitPrice = Objects.requireNonNull(unitPrice);
        if (qty == null || qty < 1) throw new IllegalArgumentException("qty >= 1");
        this.qty = qty;
    }
}
