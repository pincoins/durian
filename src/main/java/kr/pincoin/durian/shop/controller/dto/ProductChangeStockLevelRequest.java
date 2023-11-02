package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductChangeStockLevelRequest {
    @JsonProperty("minimumStockLevel")
    @NotNull
    @DecimalMin(value = "0.0")
    private Integer minimumStockLevel;

    @JsonProperty("maximumStockLevel")
    @NotNull
    @DecimalMin(value = "0.0")
    private Integer maximumStockLevel;
}
