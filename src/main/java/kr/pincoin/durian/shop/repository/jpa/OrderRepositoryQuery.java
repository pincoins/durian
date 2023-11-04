package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.conveter.*;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryQuery {

    List<Order> findOrder(Long userId,
                          OrderStatus status,
                          PaymentMethod paymentMethod,
                          PaymentStatus payment,
                          DeliveryStatus delivery,
                          OrderVisibility visibility,
                          String fullName,
                          String orderUuid,
                          String transactionId,
                          Boolean removed);

    Optional<Order> findOrder(Long id,
                              Long userId,
                              OrderStatus status,
                              PaymentMethod paymentMethod,
                              PaymentStatus payment,
                              DeliveryStatus delivery,
                              OrderVisibility visibility,
                              String fullName,
                              String orderUuid,
                              String transactionId,
                              Boolean removed);
}
