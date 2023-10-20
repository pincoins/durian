package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class PaymentMethodRequestConverter implements Converter<String, PaymentMethod> {
    @Override
    public PaymentMethod convert(@NonNull String paymentMethod) {
        return PaymentMethod.valueOf(paymentMethod.toUpperCase());
    }
}
