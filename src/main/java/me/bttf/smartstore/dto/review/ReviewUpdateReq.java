package me.bttf.smartstore.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewUpdateReq(
        @Min(1) @Max(5)
        Integer rating,

        @NotBlank
        String content
) {}

