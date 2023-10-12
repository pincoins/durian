package kr.pincoin.durian.shop.domain.conveter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter
public class PaymentAccountConverter implements AttributeConverter<PaymentAccount, Integer> {
    @Override
    public Integer convertToDatabaseColumn(PaymentAccount status) {
        if (status == null) {
            return null;
        }

        return status.getCode();
    }

    @Override
    public PaymentAccount convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(PaymentAccount.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

