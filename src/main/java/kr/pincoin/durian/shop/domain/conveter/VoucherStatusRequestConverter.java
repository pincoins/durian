package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;


public class VoucherStatusRequestConverter implements Converter<String, VoucherStatus> {
    @Override
    public VoucherStatus convert(@NonNull String description) {
        return Stream.of(VoucherStatus.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid voucher status"));
    }
}
