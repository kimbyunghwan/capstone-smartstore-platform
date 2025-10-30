<<<<<<< HEAD
package me.bttf.smartstore.domain.product;

public class Product {
=======
package me.bttf.smartstore.product;

import jakarta.persistence.*;
import lombok.Getter;
import me.bttf.smartstore.store.Store;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Product {
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;
    private String name;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private Integer rating;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
