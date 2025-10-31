package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.product.ProductOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    Page<ProductOption> findAll(Pageable pageable);
    Page<ProductOption> findByProduct_Id(Long productId, Pageable pageable);
    long countByStockQtyLessThan(int threshold);
}
