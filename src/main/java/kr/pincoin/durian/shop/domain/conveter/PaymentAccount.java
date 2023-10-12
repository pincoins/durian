package kr.pincoin.durian.shop.domain.conveter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum PaymentAccount {
    KB(0, "Kookmin Bank"),
    NH(1, "Nonghyup Bank"),
    SHINHAN(2, "Shinhan Bank"),
    WOORI(3, "Woori Bank"),
    IBK(4, "IBK Bank"),
    PAYPAL(5, "PayPal");

    private final Integer code;
    private final String description;

    PaymentAccount(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentAccount fromString(String description) {
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
