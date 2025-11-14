package me.bttf.smartstore.dto.product;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductUpdateForm {
    @NotNull
    private Long productId;

    @NotBlank
    private String productName;

    @NotBlank
    private String category;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal originalPrice;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal salePrice;

    @Min(0)
    @Max(100)
    private Integer discountRate;

    private String shortDescription;

    @NotBlank
    private String description;

    @NotNull
    @Min(0)
    private Integer stockQty = 0;

    private String option1Name;
    private String option1Values;
    private String option2Name;
    private String option2Values;

    @NotBlank
    private String deliveryType;

    private Integer deliveryFee;
    private String deliveryDescription;

    private Boolean isActive;

    private MultipartFile mainImage;
    private List<MultipartFile> detailImages;
}
