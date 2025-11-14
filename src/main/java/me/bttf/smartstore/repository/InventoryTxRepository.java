package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.inventory.InventoryTx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryTxRepository extends JpaRepository<InventoryTx, Long> {

    List<InventoryTx> findByOption_IdOrderByCreatedAtDesc(Long optionId);

    @Query("""
       select coalesce(sum(
         case when t.txType = me.bttf.smartstore.domain.inventory.TxType.IN then t.qty
              when t.txType = me.bttf.smartstore.domain.inventory.TxType.OUT then -t.qty
              else 0 end),0)
       from InventoryTx t
       where t.option.id = :optionId
    """)
    int calcNetQty(@Param("optionId") Long optionId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
      delete from InventoryTx t
      where t.option.id in (
        select o.id from ProductOption o where o.product.id in :ids
      )
    """)
    int deleteByProductIds(@Param("ids") List<Long> productIds);
}
