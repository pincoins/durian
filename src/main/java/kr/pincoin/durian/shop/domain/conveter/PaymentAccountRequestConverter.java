package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class PaymentAccountRequestConverter implements Converter<String, PaymentAccount> {
    @Override
    public PaymentAccount convert(@NonNull String paymentAccount) {
        return PaymentAccount.valueOf(paymentAccount.toUpperCase());
    }
}
