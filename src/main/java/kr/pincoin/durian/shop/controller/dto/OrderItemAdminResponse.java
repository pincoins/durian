package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.OrderItem;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemAdminResponse extends OrderItemResponse {
    @JsonProperty("userAgent")
    private String userAgent;

    @JsonProperty("acceptLanguage")
    private String acceptLanguage;

    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("visible")
    private OrderVisibility visible;

    @JsonProperty("message")
    private String message;

    @JsonProperty("suspicious")
    private boolean suspicious;

    @JsonProperty("is_removed")
    private boolean removed;

    public OrderItemAdminResponse(List<OrderItem> orderItems) {
        super(orderItems);

        Order order = orderItems.get(0).getOrder();

        this.userAgent = order.getUserAgent();
        this.acceptLanguage = order.getAcceptLanguage();
        this.ipAddress = order.getIpAddress();
        this.visible = order.getVisible();
        this.message = order.getMessage();
        this.suspicious = order.isSuspicious();
        this.removed = order.isRemoved();
    }
}
