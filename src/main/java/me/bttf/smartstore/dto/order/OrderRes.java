package me.bttf.smartstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderRes(
        Long orderId,
        Long memberId,
        String status,
        String recvName,
        String recvPhone,
        String recvZipcode,
        String recvAddr1,
        String recvAddr2,
        String recvMemo,
        BigDecimal orderTotal,
        LocalDateTime createdAt,
        List<OrderItemRes> items
) {
    public record OrderItemRes(
            Long orderItemId,
            Long productId,
            Long optionId,
            String productName,
            String optionName,
            BigDecimal unitPrice,
            int qty
    ) {}
}