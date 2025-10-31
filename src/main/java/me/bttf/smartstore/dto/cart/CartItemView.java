package me.bttf.smartstore.dto.cart;

import java.math.BigDecimal;

public record CartItemView(
        Long itemId,
        Long productId,
        Long optionId,
        String productName,
        int qty,
        BigDecimal price
) {}