package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;


public class ProductStockStatusRequestConverter implements Converter<String, ProductStockStatus> {
    @Override
    public ProductStockStatus convert(@NonNull String description) {
        return Stream.of(ProductStockStatus.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid product stock status"));
    }
}
