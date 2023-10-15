package kr.pincoin.durian.auth.domain.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;

public class UserStatusRequestConverter implements Converter<String, UserStatus> {
    @Override
    public UserStatus convert(@NonNull String description) {
        return Stream.of(UserStatus.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid user status"));
    }
}
