package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemPaymentAdminResponse extends OrderAdminResponse {
    @JsonProperty("items")
    private List<OrderItemResponse> items = new ArrayList<>();

    @JsonProperty("payments")
    private List<OrderPaymentResponse> payments = new ArrayList<>();

    public OrderItemPaymentAdminResponse(Order order) {
        super(order);

        this.items = order.getItems().stream().map(OrderItemResponse::new).toList();
        this.payments = order.getPayments().stream().map(OrderPaymentResponse::new).toList();
    }
}
