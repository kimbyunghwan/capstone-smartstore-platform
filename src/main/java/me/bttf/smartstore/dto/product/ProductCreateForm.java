package me.bttf.smartstore.dto.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductCreateForm {
    @NotBlank
    private String productName;

    @NotBlank
    private String category; // enum이면 나중에 Category enum으로

    @NotNull
    @Min(0)
    private Integer originalPrice;

    @NotNull @Min(0)
    private Integer salePrice;

    @Min(0) @Max(100)
    private Integer discountRate;

    private String shortDescription;

    @NotBlank
    private String description;

    @NotNull @Min(0)
    private Integer stockQty;

    // 옵션
    private String option1Name;
    private String option1Values; // "빨강, 파랑"
    private String option2Name;
    private String option2Values; // "S, M, L"

    // 배송
    @NotBlank private String deliveryType; // "free" | "paid"
    private Integer deliveryFee; // paid일 때만 사용
    private String deliveryDescription;

    // 이미지
    @NotNull private MultipartFile mainImage;
    private List<MultipartFile> detailImages; // name="detailImages" (multiple)

    private Boolean isActive = true;
}
