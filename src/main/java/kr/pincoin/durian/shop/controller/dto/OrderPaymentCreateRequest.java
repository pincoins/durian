package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.shop.domain.conveter.PaymentAccount;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderPaymentCreateRequest {
    @JsonProperty("paymentAccount")
    @NotNull
    private PaymentAccount account;

    @JsonProperty("amount")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
}
