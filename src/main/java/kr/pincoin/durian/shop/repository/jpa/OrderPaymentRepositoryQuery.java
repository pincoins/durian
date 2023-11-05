package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderPayment;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;

import java.util.List;

public interface OrderPaymentRepositoryQuery {
    List<OrderPayment> findOrderPayments(Long orderId,
                                         Long userId,
                                         OrderStatus status,
                                         OrderVisibility visibility,
                                         Boolean removed);
}
