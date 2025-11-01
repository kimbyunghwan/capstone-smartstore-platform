package me.bttf.smartstore.dto.product;


import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductOption;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailRes(
        Long productId,
        String name,
        String status,
        Long storeId,
        String storeName,
        String thumbnailUrl,
        BigDecimal minPrice,
        String shortDescription,
        String description,
        List<OptionRes> options,
        double rating,
        long reviewCount
) {
    public static ProductDetailRes from(Product p) {
        var min = p.getOptions().stream()
                .map(o -> o.getPrice().getAmount())
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return new ProductDetailRes(
                p.getId(),
                p.getName(),
                p.getStatus().name(),
                p.getStore() != null ? p.getStore().getId() : null,
                p.getStore() != null ? p.getStore().getStoreName() : null,
                p.getThumbnailUrl(),
                min,
                p.getShortDescription(),
                p.getDescription(),
                p.getOptions().stream().map(OptionRes::from).toList(),
                0.0,
                0L
        );
    }

    public ProductDetailRes withRating(double value) {
        return new ProductDetailRes(productId, name, status, storeId, storeName,
                thumbnailUrl, minPrice, shortDescription, description, options, value, reviewCount);
    }

    public ProductDetailRes withReviewCount(long value) {
        return new ProductDetailRes(productId, name, status, storeId, storeName,
                thumbnailUrl, minPrice, shortDescription, description, options, rating, value);
    }

    // 엔티티 필드에 맞게 매핑
    public record OptionRes(
            Long optionId,
            String sku,
            String name,
            BigDecimal price,
            Integer stockQty,
            String attrJson
    ) {
        public static OptionRes from(ProductOption o) {
            return new OptionRes(
                    o.getId(),
                    o.getSku(),
                    o.getOptionName(),
                    o.getPrice().getAmount(),
                    o.getStockQty(),
                    o.getAttrJson()
            );
        }
    }
}

