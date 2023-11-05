package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderPayment;

import java.util.List;

public interface OrderPaymentRepositoryQuery {
    List<OrderPayment> findOrderPayments(Long orderId,
                                         Long userId);
}
