package kr.pincoin.durian.auth.domain.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class PhoneVerifiedStatusRequestConverter implements Converter<String, PhoneVerifiedStatus> {
    @Override
    public PhoneVerifiedStatus convert(@NonNull String status) {
        return PhoneVerifiedStatus.valueOf(status.toUpperCase());
    }
}
