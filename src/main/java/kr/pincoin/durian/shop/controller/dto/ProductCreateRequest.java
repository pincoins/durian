package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.common.validation.NullOrNotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCreateRequest {
    @JsonProperty("slug")
    @NotEmpty
    @NotBlank
    private String slug;

    @JsonProperty("name")
    @NotEmpty
    @NotBlank
    private String name;

    @JsonProperty("subtitle")
    @NotEmpty
    @NotBlank
    private String subtitle;

    @JsonProperty("description")
    @NullOrNotBlank
    private String description;

    @JsonProperty("position")
    @NotNull
    @DecimalMin(value = "0")
    private Integer position;

    @JsonProperty("listPrice")
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal listPrice;

    @JsonProperty("sellingPrice")
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal sellingPrice;

    @JsonProperty("buyingPrice")
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal buyingPrice;

    @JsonProperty("minimumStockLevel")
    @NotNull
    @DecimalMin(value = "0.0")
    private Integer minimumStockLevel;

    @JsonProperty("maximumStockLevel")
    @NotNull
    @DecimalMin(value = "0.0")
    private Integer maximumStockLevel;

    @JsonProperty("categoryId")
    @NotNull
    private Long categoryId;
}
