package me.bttf.smartstore.dto.cart;

public record AddCartItemReq(
        Long productId,
        Long optionId,
        int qty
) {}
