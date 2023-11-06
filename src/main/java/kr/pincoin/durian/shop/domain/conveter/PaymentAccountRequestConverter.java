package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;

public class PaymentAccountRequestConverter implements Converter<String, PaymentAccount> {
    @Override
    public PaymentAccount convert(@NonNull String paymentAccount) {
        return Stream.of(PaymentAccount.values())
                .filter(c -> c.getCode().equals(paymentAccount))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Payment account not supported"));
    }
}
