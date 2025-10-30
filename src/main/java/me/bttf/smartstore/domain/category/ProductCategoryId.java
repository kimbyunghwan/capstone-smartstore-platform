package me.bttf.smartstore.domain.category;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
public class ProductCategoryId implements Serializable {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "category_id")
    private Long categoryId;

    public ProductCategoryId(Long productId, Long categoryId) {
        this.productId = productId;
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 같은 객체면 true
        if (o == null || getClass() != o.getClass()) return false; // 타입 다르면 false
        ProductCategoryId that = (ProductCategoryId) o;
        return Objects.equals(productId, that.productId)
                && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, categoryId);
    }
}
