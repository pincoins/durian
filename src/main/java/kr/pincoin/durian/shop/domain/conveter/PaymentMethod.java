package kr.pincoin.durian.shop.domain.conveter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum PaymentMethod {
    BANK_TRANSFER(0, "Bank Transfer"),
    ESCROW(1, "Escrow"),
    PAYPAL(2, "PayPal"),
    CREDIT_CARD(3, "Credit Card"),
    BANK_TRANSFER_PG(4, "Bank Transfer (PG"),
    VIRTUAL_ACCOUNT(5, "Virtual Account"),
    PHONE_BILL(6, "Phone Bill");

    private final Integer code;
    private final String description;

    PaymentMethod(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentMethod fromString(String description) {
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
