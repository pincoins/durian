package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("slug")
    @NotNull
    @NotBlank
    private String slug;

    @JsonProperty("name")
    @NotNull
    @NotBlank
    private String name;

    @JsonProperty("subtitle")
    @NotNull
    @NotBlank
    private String subtitle;

    @JsonProperty("listPrice")
    @NotNull
    private BigDecimal listPrice;

    @JsonProperty("sellingPrice")
    @NotNull
    private BigDecimal sellingPrice;

    @JsonProperty("quantity")
    @NotNull
    private Long quantity;
}
