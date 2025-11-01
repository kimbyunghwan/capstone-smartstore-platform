package me.bttf.smartstore.dto.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    @Min(0)
    private Integer originalPrice;

    @NotNull
    @Min(0)
    private Integer salePrice;

    @Min(0)
    @Max(100)
    private Integer discountRate;

    private String shortDescription;

    @NotBlank
    private String description;

    @NotNull
    @Min(0)
    private Integer stockQty;

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
