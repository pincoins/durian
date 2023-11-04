package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponse {
    @JsonProperty("orderId")
    private Long id;

    public OrderResponse(Long id) {
        this.id = id;
    }

    public OrderResponse(Order order) {
        this(order.getId());
    }
}
