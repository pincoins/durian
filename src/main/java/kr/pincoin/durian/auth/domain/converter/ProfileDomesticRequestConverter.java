package kr.pincoin.durian.auth.domain.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;

public class ProfileDomesticRequestConverter implements Converter<String, ProfileDomestic> {
    @Override
    public ProfileDomestic convert(@NonNull String description) {
        return Stream.of(ProfileDomestic.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid profile domestic status"));
    }
}
