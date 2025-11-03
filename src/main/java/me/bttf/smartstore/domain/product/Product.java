package me.bttf.smartstore.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.category.ProductCategory;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.review.Review;
import me.bttf.smartstore.domain.store.Store;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AttributeOverride(name = "id", column = @Column(name = "product_id"))
public class Product extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.INACTIVE;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(length = 200)
    private String shortDescription;

    @Lob
    private String description;

    @Builder
    public Product(Store store, String name, ProductStatus status) {
        if (store == null) throw new IllegalArgumentException("Store는 필수");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("상품명은 필수");
        this.store = store;
        this.name = name;
        this.status = status != null ? status : ProductStatus.INACTIVE;
    }

    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("상품명은 필수");
        this.name = newName;
    }

    public void changeStatus(ProductStatus status) {
        if (status == null) throw new IllegalArgumentException("상태는 필수");
        this.status = status;
    }

    public void changeThumbnail(String url) {
        this.thumbnailUrl = url;
    }

    public void changeDescriptions(String shortDesc, String desc) {
        this.shortDescription = shortDesc;
        this.description = desc;
    }

}
