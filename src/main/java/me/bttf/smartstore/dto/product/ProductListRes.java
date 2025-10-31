package me.bttf.smartstore.dto.product;

import me.bttf.smartstore.domain.product.Product;

import java.math.BigDecimal;

public record ProductListRes(
        Long productId,
        String name,
        String status,
        Long storeId,
        Integer optionCount,
        Integer reviewCount
) {
    public static ProductListRes from(Product p) {
        return new ProductListRes(
                p.getId(),
                p.getName(),
                p.getStatus().name(),
                p.getStore().getId(),
                p.getOptions() != null ? p.getOptions().size() : 0,
                p.getReviews() != null ? p.getReviews().size() : 0
        );
    }
}
