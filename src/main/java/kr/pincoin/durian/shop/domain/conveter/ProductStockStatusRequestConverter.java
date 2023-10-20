package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;


public class ProductStockStatusRequestConverter implements Converter<String, ProductStockStatus> {
    @Override
    public ProductStockStatus convert(@NonNull String stock) {
        return ProductStockStatus.valueOf(stock.toUpperCase());
    }
}
