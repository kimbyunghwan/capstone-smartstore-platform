package me.bttf.smartstore.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.common.Money;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "product_option",
        indexes = {
                @Index(name = "ix_product_option_product", columnList = "product_id"),
                @Index(name = "ix_product_option_sku", columnList = "sku")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "option_id"))
public class ProductOption extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "sku", nullable = false, length = 100, unique = true)
    private String sku; // 옵션별 고유 SKU

    @Column(name = "option_name", length = 100)
    private String optionName; // 옵션명 (ex. 블랙 / M)

    @Column(name = "attr_json", columnDefinition = "json")
    private String attrJson; // JSON으로 옵션 상세 속성(색상, 사이즈 등)

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price", precision = 14, scale = 2, nullable = false))
    })
    private Money price; // 옵션별 가격

    @Column(name = "stock_qty", nullable = false)
    private Integer stockQty = 0; // 현재 재고 수량 (default 0)

    @Builder
    private ProductOption(Product product, String sku, String optionName, String attrJson, Money price, Integer stockQty) {
        if (product == null) throw new IllegalArgumentException("product는 필수");
        if (sku == null || sku.isBlank()) throw new IllegalArgumentException("sku는 필수");
        if (price == null) throw new IllegalArgumentException("price는 필수");
        this.product = product;
        this.sku = sku;
        this.optionName = optionName;
        this.attrJson = attrJson;
        this.price = price;
        this.stockQty = (stockQty != null) ? stockQty : 0;
    }

    public void updateStockQty(int newQty) {
        this.stockQty = newQty;
    }
}
