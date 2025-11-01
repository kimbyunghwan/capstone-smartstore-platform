package me.bttf.smartstore.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateReq(

        @NotNull
        Long memberId,

        @Min(1) @Max(5)
        int rating,

        @NotBlank
        String content
) {}
