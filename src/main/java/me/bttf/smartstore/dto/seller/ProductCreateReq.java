package me.bttf.smartstore.dto.seller;

import java.util.List;

public record ProductCreateReq(
        Long storeId,
        String name,
        Long categoryId,
        boolean primary,
        List<SkuReq> skus
) {}
