package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import kr.pincoin.durian.common.validation.NullOrNotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryUpdateRequest {
    @JsonProperty("title")
    @NullOrNotBlank
    private String title;

    @JsonProperty("slug")
    @NullOrNotBlank
    private String slug;

    @JsonProperty("description")
    @NullOrNotBlank
    private String description;

    @JsonProperty("subDescription")
    @NullOrNotBlank
    private String subDescription;

    @JsonProperty("discountRate")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal discountRate;

    @JsonProperty("position")
    @DecimalMin(value = "0")
    private Integer position;
}
