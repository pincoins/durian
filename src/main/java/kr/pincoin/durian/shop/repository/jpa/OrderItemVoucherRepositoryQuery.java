package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderItemVoucher;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemVoucherDto;

import java.util.List;

public interface OrderItemVoucherRepositoryQuery {
    int saveAll(List<OrderItemVoucherDto> orderItemVouchers);

    List<OrderItemVoucher> findOrderItemVouchers(Long orderId,
                                                 Long userId,
                                                 Long itemId,
                                                 OrderStatus status,
                                                 OrderVisibility visibility,
                                                 Boolean removed);
}
