package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductStatus;
import me.bttf.smartstore.dto.inventory.InventoryRowRes;
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

    @Query("""
        select new me.bttf.smartstore.dto.inventory.InventoryRowRes(
          p.id,
          p.name,
          coalesce(p.thumbnailUrl, '/img/placeholder.png'),
          coalesce(cast(min(o.price.amount) as bigdecimal), cast(0 as bigdecimal)),
          coalesce(sum(coalesce(o.stockQty,0)), 0),
          p.status,
          coalesce(
            (select c.name from ProductCategory pc join pc.category c
              where pc.product = p and pc.primaryCategory = true),
            '-'
          ),
          case when coalesce(sum(coalesce(o.stockQty,0)),0) <= 0 then true else false end,
          case when coalesce(sum(coalesce(o.stockQty,0)),0) > 0
                and sum(case when o.lowStockThreshold is not null and o.lowStockThreshold > 0
                              and coalesce(o.stockQty,0) <= o.lowStockThreshold then 1 else 0 end) > 0 then true else false end
        )
        from Product p
        left join p.options o
        where p.store.id = :storeId
          and (:q is null or :q = ''
               or lower(p.name) like lower(concat('%', :q, '%'))
               or cast(p.id as string) like concat('%', :q, '%'))
        group by p.id, p.name, p.thumbnailUrl
        order by p.createdAt desc
        """)
    Page<InventoryRowRes> findInventory(@Param("storeId") Long storeId,
                                        @Param("q") String q,
                                        Pageable pageable);

    // 품절 개수 (상품 단위)
    @Query("""
        select count(distinct p.id)
        from Product p
        join p.options o
        where p.store.id = :storeId
        group by p.id
        having coalesce(sum(coalesce(o.stockQty,0)),0) <= 0
        """)
    Long countOutOfStock(@Param("storeId") Long storeId);


    // 재고 부족 개수 (상품 단위)
    // 조건: 총 재고 > 0 이고, 옵션 중 (임계치 > 0 && 재고 <= 임계치) 인 것이 하나 이상
    @Query("""
        select count(distinct p.id)
        from Product p
        join p.options o
        where p.store.id = :storeId
        group by p.id
        having coalesce(sum(coalesce(o.stockQty,0)),0) > 0
           and sum(
                case when o.lowStockThreshold is not null and o.lowStockThreshold > 0
                          and coalesce(o.stockQty,0) <= o.lowStockThreshold
                     then 1 else 0 end
               ) > 0
        """)
    Long countLowStock(@Param("storeId") Long storeId);

    // 내(strore.owner.id) 상품만 조회
    Optional<Product> findByIdAndStore_Owner_Id(Long productId, Long ownerId);
}
