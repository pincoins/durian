package kr.pincoin.durian.shop.domain.conveter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum VoucherStatus {
    PURCHASED(0, "enabled"),
    SOLD(1, "disabled"),
    REVOKED(2, "revoked");

    private final Integer code;
    private final String description;

    VoucherStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static VoucherStatus fromString(String description) {
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
