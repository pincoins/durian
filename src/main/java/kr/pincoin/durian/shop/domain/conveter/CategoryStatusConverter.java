package kr.pincoin.durian.shop.domain.conveter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter
public class CategoryStatusConverter implements AttributeConverter<CategoryStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(CategoryStatus status) {
        if (status == null) {
            return null;
        }

        return status.getCode();
    }

    @Override
    public CategoryStatus convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(CategoryStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

