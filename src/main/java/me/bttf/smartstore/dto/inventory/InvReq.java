package me.bttf.smartstore.dto.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InvReq(
        @NotNull Long optionId,
        @NotNull @Min(0) Integer qty,
        String reason
) {}