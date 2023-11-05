package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderItem;

import java.util.List;

public interface OrderItemRepositoryQuery {
    List<OrderItem> findOrderItems(Long orderId,
                                   Long userId,
                                   Boolean removed);
}
