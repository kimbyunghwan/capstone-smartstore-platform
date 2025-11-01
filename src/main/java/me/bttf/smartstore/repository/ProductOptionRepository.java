package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductOption;
import me.bttf.smartstore.domain.product.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    Page<ProductOption> findAll(Pageable pageable);
    Page<ProductOption> findByProduct_Id(Long productId, Pageable pageable);

    List<ProductOption> findByProduct_Id(Long productId);

    long countByStockQtyLessThan(int threshold);
}
