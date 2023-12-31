package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class CategoryStatusRequestConverter implements Converter<String, CategoryStatus> {
    @Override
    public CategoryStatus convert(@NonNull String status) {
        return CategoryStatus.valueOf(status.toUpperCase());
    }
}
