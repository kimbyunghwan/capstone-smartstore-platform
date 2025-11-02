package me.bttf.smartstore.dto.auth;

import me.bttf.smartstore.domain.member.MemberType;

public record RegisterReq(
        String email,
        String password,
        String name,
        String phone,
        MemberType memberType, // BUYER or SELLER
        String storeName // 판매자만
) {}