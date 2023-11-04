package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderAdminResponse extends OrderResponse {
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

    public OrderAdminResponse(Order order) {
        super(order.getId(),
              order.getOrderUuid(),
              order.getFullName(),
              order.getTotalListPrice(),
              order.getTotalSellingPrice(),
              order.getTransactionId(),
              order.getCreated(),
              order.getModified(),
              order.getStatus(),
              order.getPaymentMethod(),
              order.getPayment(),
              order.getDelivery());

        this.userAgent = order.getUserAgent();
        this.acceptLanguage = order.getAcceptLanguage();
        this.ipAddress = order.getIpAddress();
        this.visible = order.getVisible();
        this.message = order.getMessage();
        this.suspicious = order.isSuspicious();
        this.removed = order.isRemoved();
    }
}
