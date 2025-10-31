package me.bttf.smartstore.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderReq(
        @NotNull
        Long memberId,

        Long recvAddressId,

        @NotBlank
        String recvName,

        @NotBlank
        String recvPhone,

        @NotBlank
        String recvZipcode,

        @NotBlank
        String recvAddr1,

        String recvAddr2,
        String recvMemo,

        @NotEmpty
        List<Item> items
) {
    public record Item(
            @NotNull
            Long optionId,

            @Min(1)
            int qty
    ) {}
}
