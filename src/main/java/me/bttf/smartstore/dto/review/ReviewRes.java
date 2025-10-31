package me.bttf.smartstore.dto.review;

import java.time.LocalDateTime;

public record ReviewRes(
        Long reviewId,
        Long productId,
        Long memberId,
        int rating,
        String content,
        LocalDateTime createdAt
) {}
