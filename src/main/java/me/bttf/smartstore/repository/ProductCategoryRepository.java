package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.category.ProductCategory;
import me.bttf.smartstore.domain.category.ProductCategoryId;
import me.bttf.smartstore.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {

    @Query("""
        select pc.category.id
        from ProductCategory pc
        where pc.product.id = :productId
          and pc.primaryCategory = true
    """)
    Optional<Long> findPrimaryCategoryId(@Param("productId") Long productId);

    // 카테고리 여러 개(하위 포함)로 상품 페이징
    @Query("""
        select pc.product from ProductCategory pc
        where pc.category.id in :categoryIds
        """)
    Page<Product> findProductsByCategoryIds(
            @Param("categoryIds") Collection<Long> categoryIds,
            Pageable pageable);

    @Modifying
    @Query("update ProductCategory pc " +
            "set pc.primaryCategory=false " +
            "where pc.product.id=:productId and pc.primaryCategory=true")
    int clearPrimary(@Param("productId") Long productId);

    boolean existsById(ProductCategoryId id);
}
