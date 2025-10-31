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
        List<OptionRes> options
) {
    public static ProductDetailRes from(Product p) {
        return new ProductDetailRes(
                p.getId(),
                p.getName(),
                p.getStatus().name(),
                p.getStore().getId(),
                p.getOptions().stream().map(OptionRes::from).toList()
        );
    }

    // 옵션 DTO: 엔티티 필드에 맞게 매핑
    public record OptionRes(
            Long optionId,
            String sku,
            String name,           // optionName
            BigDecimal price,      // price.amount
            Integer stockQty,
            String attrJson
    ) {
        public static OptionRes from(ProductOption o) {
            return new OptionRes(
                    o.getId(),
                    o.getSku(),
                    o.getOptionName(),
                    o.getPrice().getAmount(), // Money의 amount getter 사용
                    o.getStockQty(),
                    o.getAttrJson()
            );
        }
    }
}

