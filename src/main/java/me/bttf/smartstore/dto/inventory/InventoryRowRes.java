package me.bttf.smartstore.dto.inventory;

import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductOption;
import me.bttf.smartstore.domain.product.ProductStatus;

import java.math.BigDecimal;

public record InventoryRowRes(
        Long optionId,
        Long productId,
        String name,
        String imageUrl,
        BigDecimal salePrice,
        Integer stockQty,
        ProductStatus status,
        boolean soldOut,
        boolean low
) {

    public static InventoryRowRes from(ProductOption o) {
        Product p = o.getProduct();

        return new InventoryRowRes(
                o.getId(),                               // optionId
                p.getId(),                               // productId
                p.getName(),                             // name
                p.getThumbnailUrl(),                     // imageUrl
                o.getPrice() != null ? o.getPrice().getAmount() : BigDecimal.ZERO, // salePrice
                o.getStockQty(),                         // stockQty
                p.getStatus(),                           // status
                o.getStockQty() == 0,                    // soldOut
                o.isLowStock()                           // low (ProductOption에서 로직 정의)
        );
    }
}