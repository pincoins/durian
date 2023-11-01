package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryCreateRequest {
    @JsonProperty("title")
    @NotEmpty
    @NotBlank
    private String title;

    @JsonProperty("slug")
    @NotEmpty
    @NotBlank
    private String slug;

    @JsonProperty("description")
    @NotEmpty
    @NotBlank
    private String description;

    @JsonProperty("subDescription")
    @NotEmpty
    @NotBlank
    private String subDescription;

    @JsonProperty("discountRate")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal discountRate;
}
