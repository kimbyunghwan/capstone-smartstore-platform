package me.bttf.smartstore.repository;

import me.bttf.smartstore.InventoryRowView;
import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductOption;
import me.bttf.smartstore.domain.product.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    Page<ProductOption> findAll(Pageable pageable);
    Page<ProductOption> findByProduct_Id(Long productId, Pageable pageable);

    List<ProductOption> findByProduct_Id(Long productId);

    long countByStockQtyLessThan(int threshold);

    @Query("""
    select
      o.id                                         as optionId,
      p.id                                         as productId,
      p.name                                       as name,
      p.thumbnailUrl                               as imageUrl,
      coalesce(c.name, '')                         as category,
      o.price.amount                               as salePrice,
      o.stockQty                                   as stockQty,
      case 
        when o.stockQty = 0 then true 
        else false 
      end                                          as soldOut,
      case 
        when o.lowStockThreshold is not null and o.stockQty <= o.lowStockThreshold then true
        else false
      end                                          as low
    from ProductOption o
      join o.product p
      left join ProductCategory pc on pc.product = p and pc.primaryCategory = true
      left join pc.category c
    where p.store.id = :storeId
    order by p.id desc, o.id asc
    """)
    List<InventoryRowView> findInventoryRows(@Param("storeId") Long storeId);
}
