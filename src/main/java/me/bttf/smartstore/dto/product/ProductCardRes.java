package me.bttf.smartstore.dto.product;

import java.math.BigDecimal;

public record ProductCardRes(
        Long productId,
        String name,
        String imageUrl,
        BigDecimal salePrice
) {}