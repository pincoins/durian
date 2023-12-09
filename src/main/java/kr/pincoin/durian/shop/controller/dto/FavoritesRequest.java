package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritesRequest {
    @JsonProperty("userId")
    @NotNull
    private Long userId;

    @JsonProperty("productId")
    @NotNull
    private Long productId;
}
