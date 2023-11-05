package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.OrderPayment;
import kr.pincoin.durian.shop.domain.conveter.PaymentAccount;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderPaymentResponse {
    @JsonProperty("account")
    private PaymentAccount account;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("received")
    private LocalDateTime received;

    public OrderPaymentResponse(OrderPayment orderPayment) {
        this.account = orderPayment.getAccount();
        this.amount = orderPayment.getAmount();
        this.balance = orderPayment.getBalance();
        this.received = orderPayment.getReceived();
    }
}
