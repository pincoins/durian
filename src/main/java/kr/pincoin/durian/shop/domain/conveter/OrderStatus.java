package kr.pincoin.durian.shop.domain.conveter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum OrderStatus {
    PAYMENT_PENDING(0, "Payment Pending"),
    PAYMENT_COMPLETED(1, "Payment Completed"),
    UNDER_REVIEW(2, "Under Review"),
    PAYMENT_VERIFIED(3, "Payment Verified"),
    SHIPPED(4, "Shipped"),
    REFUND_REQUESTED(5, "Refund Requested"),
    REFUND_PENDING(6, "Refund Pending"),
    REFUNDED1(7, "Refunded1"),
    REFUNDED2(8, "Refunded2"),
    VOIDED(9, "Voided");

    private final Integer code;
    private final String description;

    OrderStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OrderStatus fromString(String description) {
        return Stream.of(values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
