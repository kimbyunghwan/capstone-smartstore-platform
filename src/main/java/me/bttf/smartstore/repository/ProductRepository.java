package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductStatus;
import me.bttf.smartstore.dto.product.ProductCardRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String q, Pageable pageable);

    List<Product> findTop12ByStatusOrderByCreatedAtDesc(ProductStatus status);

    @EntityGraph(attributePaths = {"store", "options"})
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findDetailById(@Param("id") Long id);


    @Query("""
    select new me.bttf.smartstore.dto.product.ProductCardRes(
        p.id,
        p.name,
        coalesce(p.thumbnailUrl, '/img/placeholder.png'),
        coalesce(
            (select cast(min(o2.price.amount) as bigdecimal)
               from ProductOption o2
              where o2.product = p),
            cast(0 as bigdecimal)
        )
    )
    from Product p
    where p.status = :status
    order by p.createdAt desc
    """)
    List<ProductCardRes> findTopCards(@Param("status") ProductStatus status, Pageable pageable);

    @Query("""
    select new me.bttf.smartstore.dto.product.ProductCardRes(
        p.id,
        p.name,
        coalesce(p.thumbnailUrl, '/img/placeholder.png'),
        coalesce(
            (select cast(min(o2.price.amount) as bigdecimal)
               from ProductOption o2
              where o2.product = p),
            cast(0 as bigdecimal)
        )
    )
    from Product p
    where p.status = :status
    order by p.createdAt desc
    """)
    List<ProductCardRes> findTopBest(@Param("status") ProductStatus status, Pageable pageable);
}
