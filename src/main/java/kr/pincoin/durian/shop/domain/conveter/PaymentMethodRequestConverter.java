package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;

public class PaymentMethodRequestConverter implements Converter<String, PaymentMethod> {
    @Override
    public PaymentMethod convert(@NonNull String description) {
        return Stream.of(PaymentMethod.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment method"));
    }
}
