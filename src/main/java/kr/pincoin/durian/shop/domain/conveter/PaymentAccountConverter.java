package kr.pincoin.durian.shop.domain.conveter;

import jakarta.persistence.AttributeConverter;

import java.util.stream.Stream;

public class PaymentAccountConverter implements AttributeConverter<PaymentAccount, String> {
    @Override
    public String convertToDatabaseColumn(PaymentAccount account) {
        if (account == null) {
            return null;
        }

        return account.getCode();
    }

    @Override
    public PaymentAccount convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(PaymentAccount.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
