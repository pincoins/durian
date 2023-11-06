package kr.pincoin.durian.shop.domain.conveter;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum PaymentAccount {
    KB("0"),
    NH("1"),
    SHINHAN("2"),
    WOORI("3"),
    IBK("4"),
    PAYPAL("5"),
    CREDIT_CARD("6"),
    PHONE_BILL("7");

    private final String code;

    PaymentAccount(String code) {
        this.code = code;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentAccount fromCode(String code) {
        return Stream.of(values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
