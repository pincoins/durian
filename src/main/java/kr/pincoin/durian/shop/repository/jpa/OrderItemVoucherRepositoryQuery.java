package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderItemVoucher;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;

import java.util.List;

public interface OrderItemVoucherRepositoryQuery {
    List<OrderItemVoucher> findOrderItemVouchers(Long orderId,
                                                 Long userId,
                                                 Long itemId,
                                                 OrderStatus status,
                                                 OrderVisibility visibility,
                                                 Boolean removed);
}
