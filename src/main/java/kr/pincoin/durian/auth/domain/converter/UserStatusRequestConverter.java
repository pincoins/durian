package kr.pincoin.durian.auth.domain.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class UserStatusRequestConverter implements Converter<String, UserStatus> {
    @Override
    public UserStatus convert(@NonNull String status) {
        return UserStatus.valueOf(status.toUpperCase());
    }
}
