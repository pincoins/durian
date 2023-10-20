package kr.pincoin.durian.auth.domain.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class ProfileGenderRequestConverter implements Converter<String, ProfileGender> {
    @Override
    public ProfileGender convert(@NonNull String gender) {
        return ProfileGender.valueOf(gender.toUpperCase());
    }
}
