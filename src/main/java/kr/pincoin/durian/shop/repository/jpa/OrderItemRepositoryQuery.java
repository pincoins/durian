package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderItem;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemCountResult;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemProductResult;

import java.util.List;

public interface OrderItemRepositoryQuery {
    List<OrderItem> findOrderItems(Long orderId,
                                   Long userId,
                                   OrderStatus status,
                                   OrderVisibility visibility,
                                   Boolean removed);

    List<OrderItem> findOrderItemsWithVouchers(Long orderId,
                                               Long userId,
                                               OrderStatus status,
                                               OrderVisibility visibility,
                                               Boolean removed);

    List<OrderItemProductResult> findOrderItemWithProduct(Long orderId,
                                                          Long userId);

   List<OrderItemCountResult> findVoucherCountByOrderItem(Long orderId,
                                                          Long userId);
}
