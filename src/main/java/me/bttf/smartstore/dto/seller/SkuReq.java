package me.bttf.smartstore.dto.seller;

import java.math.BigDecimal;

public record SkuReq(
        String sku,
        String optionName,
        String attrJson,
        BigDecimal price,
        Integer stock
) {}
