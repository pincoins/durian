package kr.pincoin.durian.shop.repository.jpa.dto;

import kr.pincoin.durian.shop.domain.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemCountResult {
    private OrderItem orderItem;

    private Long count;
}
