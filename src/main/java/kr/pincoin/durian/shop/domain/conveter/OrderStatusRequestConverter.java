package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class OrderStatusRequestConverter implements Converter<String, OrderStatus> {
    @Override
    public OrderStatus convert(@NonNull String status) {
        return OrderStatus.valueOf(status.toUpperCase());
    }
}
