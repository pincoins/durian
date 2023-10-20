package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class OrderVisibilityRequestConverter implements Converter<String, OrderVisibility> {
    @Override
    public OrderVisibility convert(@NonNull String visibility) {
        return OrderVisibility.valueOf(visibility.toUpperCase());
    }
}
