package kr.pincoin.durian.shop.domain.conveter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter
public class ProductStockStatusConverter implements AttributeConverter<ProductStockStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductStockStatus status) {
        if (status == null) {
            return null;
        }

        return status.getCode();
    }

    @Override
    public ProductStockStatus convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Stream.of(ProductStockStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

