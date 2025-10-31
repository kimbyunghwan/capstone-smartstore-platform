package me.bttf.smartstore.dto.auth;

public record RegisterReq(
        String email,
        String password,
        String name
) {}