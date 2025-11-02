package me.bttf.smartstore.dto.seller;

import me.bttf.smartstore.domain.product.ProductStatus;

import java.util.List;

public record ProductCreateReq(
        String name,
        String shortDescription,
        String description,
        Long categoryId,
        boolean primary,
        List<SkuReq> skus,
        ProductStatus status
) {}
