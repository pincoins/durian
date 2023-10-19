package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;

public class CategoryStatusRequestConverter implements Converter<String, CategoryStatus> {
    @Override
    public CategoryStatus convert(@NonNull String description) {
        return Stream.of(CategoryStatus.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category status"));
    }
}
