package me.bttf.smartstore.dto.inventory;

import me.bttf.smartstore.domain.product.ProductStatus;

import java.math.BigDecimal;

public record InventoryRowRes(
        Long productId,
        String name,
        String imageUrl,
        BigDecimal salePrice,
        Long stockQty,
        ProductStatus status,
        String category,
        boolean soldOut,
        boolean low
) {}