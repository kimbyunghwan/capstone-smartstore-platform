package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductOption;
import me.bttf.smartstore.dto.inventory.InventoryRowRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    Page<ProductOption> findAll(Pageable pageable);
    Page<ProductOption> findByProduct_Id(Long productId, Pageable pageable);

    List<ProductOption> findByProduct_Id(Long productId);

    long countByStockQtyLessThan(int threshold);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update ProductOption o
           set o.stockQty = :qty,
               o.updatedAt = CURRENT_TIMESTAMP
         where o.id = :optionId
           and o.product.store.owner.id = :ownerId
        """)
    int updateStockQtyByOwner(@Param("optionId") Long optionId, @Param("qty") Integer qty, @Param("ownerId") Long ownerId);

    boolean existsByIdAndProductStoreOwnerId(Long id, Long ownerId);

    @Query("""
    select new me.bttf.smartstore.dto.inventory.InventoryRowRes(
        o.id,
        p.id,
        p.name,
        p.thumbnailUrl,
        o.price.amount,
        cast(coalesce(o.stockQty, 0) as long),
        p.status,
        coalesce(c.name, ''),
        case when coalesce(o.stockQty, 0) = 0 then true else false end,
        case when coalesce(o.lowStockThreshold, 0) > 0
                  and coalesce(o.stockQty, 0) <= coalesce(o.lowStockThreshold, 0)
             then true else false end
    )
    from ProductOption o
    join o.product p
    left join ProductCategory pc on pc.product = p and pc.primaryCategory = true
    left join pc.category c
    where p.store.owner.id = :ownerId
    order by p.createdAt desc
    """)
    List<InventoryRowRes> findInventoryRowsByOwnerId(@Param("ownerId") Long ownerId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ProductOption o where o.product.id in :ids")
    int deleteByProductIds(@Param("ids") List<Long> ids);

}
