package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;


public class ProductStatusRequestConverter implements Converter<String, ProductStatus> {
    @Override
    public ProductStatus convert(@NonNull String description) {
        return Stream.of(ProductStatus.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid product status"));
    }
}
