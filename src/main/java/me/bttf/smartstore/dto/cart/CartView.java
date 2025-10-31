package me.bttf.smartstore.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartView(
        List<CartItemView> items,
        int totalQty,
        BigDecimal totalPrice
) {}
