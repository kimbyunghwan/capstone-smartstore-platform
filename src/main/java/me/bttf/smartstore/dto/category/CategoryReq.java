package me.bttf.smartstore.dto.category;

public record CategoryReq(String name, Long parentId, Integer depth) {}
