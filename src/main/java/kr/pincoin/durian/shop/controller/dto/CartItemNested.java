package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemNested {
    @JsonProperty("productId")
    @NotNull
    private Long productId;

    @JsonProperty("quantity")
    @NotNull
    private Integer quantity;
}
