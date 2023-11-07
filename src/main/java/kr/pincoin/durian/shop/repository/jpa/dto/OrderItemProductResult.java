package kr.pincoin.durian.shop.repository.jpa.dto;

import kr.pincoin.durian.shop.domain.OrderItem;
import kr.pincoin.durian.shop.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemProductResult {
    private OrderItem orderItem;

    private Product product;

    private Long remaining;
}
