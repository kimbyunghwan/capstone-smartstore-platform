package me.bttf.smartstore.dto;

public record BootpayVerifyReq(
        String orderId,
        String receiptId,
        Long optionId,
        Integer qty,
        java.math.BigDecimal amount
) {}