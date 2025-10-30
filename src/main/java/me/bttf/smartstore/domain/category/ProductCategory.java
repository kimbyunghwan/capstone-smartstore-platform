package me.bttf.smartstore.domain.category;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.product.Product;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "product_category",
        indexes = {
                @Index(name = "ix_pc_product", columnList = "product_id"),
                @Index(name = "ix_pc_category", columnList = "category_id")
        }
)
public class ProductCategory {

    @EmbeddedId
    private ProductCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId") // PK의 productId 필드를 이 FK에 매핑
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId") // PK의 categoryId 필드를 이 FK에 매핑
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "is_primary", nullable = false)
    private boolean primaryCategory;

    public ProductCategory(Product product, Category category, boolean primaryCategory) {
        this.product = product;
        this.category = category;
        this.id = new ProductCategoryId(product.getId(), category.getId());
        this.primaryCategory = primaryCategory;
    }
}
