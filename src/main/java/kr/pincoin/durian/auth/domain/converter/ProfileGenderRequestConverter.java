package kr.pincoin.durian.auth.domain.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;

public class ProfileGenderRequestConverter implements Converter<String, ProfileGender> {
    @Override
    public ProfileGender convert(@NonNull String description) {
        return Stream.of(ProfileGender.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid profile gender"));
    }
}
