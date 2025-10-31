package me.bttf.smartstore.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReviewUpdateReq(
        @Min(1) @Max(5)
        Integer rating,

        String content
) {}

