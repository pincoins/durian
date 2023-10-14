package kr.pincoin.durian.auth.domain.converter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum ProfileGender {
    FEMALE(0, "Female"),
    MALE(1, "Male");

    private final Integer code;
    private final String description;

    ProfileGender(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ProfileGender fromString(String description) {
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
