package kr.pincoin.durian.shop.domain.conveter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter
public class OrderVisibilityConverter implements AttributeConverter<OrderVisibility, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OrderVisibility status) {
        if (status == null) {
            return null;
        }

        return status.getCode();
    }

    @Override
    public OrderVisibility convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(OrderVisibility.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

