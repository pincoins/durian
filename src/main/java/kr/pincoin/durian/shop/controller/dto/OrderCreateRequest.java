package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.shop.domain.conveter.PaymentMethod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderCreateRequest {
    @JsonProperty("userId")
    @NotNull
    private Long userId;

    @JsonProperty("paymentMethod")
    @NotNull
    private PaymentMethod paymentMethod;

    @JsonProperty("items")
    @NotNull
    private List<CartItemNested> items = new ArrayList<>();
}
