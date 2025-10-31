package me.bttf.smartstore.dto.auth;

public record LoginReq(
        String email,
        String password
) {}
