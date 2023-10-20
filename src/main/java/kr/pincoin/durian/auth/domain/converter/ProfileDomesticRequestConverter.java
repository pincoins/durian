package kr.pincoin.durian.auth.domain.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class ProfileDomesticRequestConverter implements Converter<String, ProfileDomestic> {
    @Override
    public ProfileDomestic convert(@NonNull String status) {
        return ProfileDomestic.valueOf(status.toUpperCase());
    }
}
