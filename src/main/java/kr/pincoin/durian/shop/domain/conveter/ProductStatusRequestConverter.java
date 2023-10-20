package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;


public class ProductStatusRequestConverter implements Converter<String, ProductStatus> {
    @Override
    public ProductStatus convert(@NonNull String status) {
        return ProductStatus.valueOf(status.toUpperCase());
    }
}
