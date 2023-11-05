package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.conveter.DeliveryStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.PaymentMethod;
import kr.pincoin.durian.shop.domain.conveter.PaymentStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponse {
    @JsonProperty("orderId")
    private Long id;

    @JsonProperty("orderUuid")
    private String orderUuid;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("totalListPrice")
    private BigDecimal totalListPrice;

    @JsonProperty("totalSellingPrice")
    private BigDecimal totalSellingPrice;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonProperty("modified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modified;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("paymentMethod")
    private PaymentMethod paymentMethod;

    @JsonProperty("payment")
    private PaymentStatus payment;

    @JsonProperty("delivery")
    private DeliveryStatus delivery;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderUuid = order.getOrderUuid();
        this.fullName = order.getFullName();
        this.totalListPrice = order.getTotalListPrice();
        this.totalSellingPrice = order.getTotalSellingPrice();
        this.transactionId = order.getTransactionId();
        this.created = order.getCreated();
        this.modified = order.getModified();
        this.status = order.getStatus();
        this.paymentMethod = order.getPaymentMethod();
        this.payment = order.getPayment();
        this.delivery = order.getDelivery();
    }
}
