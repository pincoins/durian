package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.conveter.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryQuery {
    List<Order> findOrders(Long userId,
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

    Optional<Order> findOrderWithPayments(Long id,
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

    List<Order> findDistinctOrderWithItems(Long id,
                                           Long userId,
                                           OrderStatus status,
                                           PaymentMethod paymentMethod,
                                           PaymentStatus payment,
                                           DeliveryStatus delivery,
                                           OrderVisibility visibility,
                                           String fullName,
                                           BigDecimal totalSellingPrice,
                                           String orderUuid,
                                           String transactionId,
                                           Boolean removed);
}
