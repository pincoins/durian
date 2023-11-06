package kr.pincoin.durian.shop.domain.conveter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum PaymentAccount {
    KB("0", "KB"),
    NH("1", "NH"),
    SHINHAN("2", "SHINHAN"),
    WOORI("3", "WOORI"),
    IBK("4", "IBK"),
    PAYPAL("5", "PAYPAL"),
    CREDIT_CARD("6", "CREDIT_CARD"),
    PHONE_BILL("7", "PHONE_BILL");

    private final String code;
    private final String description;

    PaymentAccount(String code, String description) {
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
