<<<<<<< HEAD
package me.bttf.smartstore.domain.category;

public class ProductCategory {
=======
package me.bttf.smartstore.category;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import me.bttf.smartstore.common.BaseEntity;
import me.bttf.smartstore.product.Product;

@Entity
@Getter
public class ProductCategory extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    private boolean primaryCategory;
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
