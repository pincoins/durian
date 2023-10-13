package kr.pincoin.durian.auth.domain.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;

public class PhoneVerifiedStatusRequestConverter implements Converter<String, PhoneVerifiedStatus> {
    @Override
    public PhoneVerifiedStatus convert(@NonNull String description) {
        return Stream.of(PhoneVerifiedStatus.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid phone verification status"));
    }
}
