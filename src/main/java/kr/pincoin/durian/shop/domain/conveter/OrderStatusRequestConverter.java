package kr.pincoin.durian.shop.domain.conveter;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

import java.util.stream.Stream;

public class OrderStatusRequestConverter implements Converter<String, OrderStatus> {
    @Override
    public OrderStatus convert(@NonNull String description) {
        return Stream.of(OrderStatus.values())
                .filter(c -> c.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment method"));
    }
}
