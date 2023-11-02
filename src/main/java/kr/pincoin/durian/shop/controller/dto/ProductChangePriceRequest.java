package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductChangePriceRequest {
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
}
