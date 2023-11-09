package kr.pincoin.durian.auth.domain.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class ProfileTelecomRequestConverter implements Converter<String, ProfileTelecom> {
    @Override
    public ProfileTelecom convert(@NonNull String telecom) {
        return ProfileTelecom.valueOf(telecom.toUpperCase());
    }
}
