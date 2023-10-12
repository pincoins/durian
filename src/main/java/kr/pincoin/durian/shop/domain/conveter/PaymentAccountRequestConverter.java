package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;

public class PaymentAccountRequestConverter implements Converter<String, PaymentAccount> {
    @Override
    public PaymentAccount convert(@NonNull String description) {
        return Stream.of(PaymentAccount.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment method"));
    }
}
