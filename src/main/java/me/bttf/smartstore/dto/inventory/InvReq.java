package me.bttf.smartstore.dto.inventory;

public record InvReq(
        int qty,
        String reason
) {}