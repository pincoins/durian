package kr.pincoin.durian.auth.domain.converter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class VerificationStatusRequestConverter implements Converter<String, VerificationStatus> {
    @Override
    public VerificationStatus convert(@NonNull String status) {
        return VerificationStatus.valueOf(status.toUpperCase());
    }
}
