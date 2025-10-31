package me.bttf.smartstore.dto.seller;

import me.bttf.smartstore.domain.product.ProductStatus;

public record ProductUpdateReq(
        String name,
        ProductStatus status
) {}