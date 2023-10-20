package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class VoucherStatusRequestConverter implements Converter<String, VoucherStatus> {
    @Override
    public VoucherStatus convert(@NonNull String status) {
        return VoucherStatus.valueOf(status.toUpperCase());
    }
}
