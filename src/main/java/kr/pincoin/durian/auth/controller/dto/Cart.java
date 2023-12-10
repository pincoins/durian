package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Cart {
    @JsonProperty("items")
    @NotNull
    private List<CartItem> items = new ArrayList<>();

    public static CartBuilder builder() {
        return new CartBuilder().items(List.of());
    }
}
