package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.conveter.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.shop.domain.QOrder.order1;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Order> findOrder(Long userId,
                                 OrderStatus status,
                                 PaymentMethod paymentMethod,
                                 PaymentStatus payment,
                                 DeliveryStatus delivery,
                                 OrderVisibility visibility,
                                 String fullName,
                                 String orderUuid,
                                 String transactionId,
                                 Boolean removed) {
        JPAQuery<Order> contentQuery = queryFactory
                .select(order1)
                .from(order1)
                .where(userIdEq(userId),
                       statusEq(status),
                       paymentMethodEq(paymentMethod),
                       paymentEq(payment),
                       deliverEq(delivery),
                       visibilityEq(visibility),
                       fullNameContains(fullName),
                       orderUuidContains(orderUuid),
                       transactionIdContains(transactionId),
                       removedEq(removed));

        return contentQuery.fetch();
    }

    @Override
    public Optional<Order> findOrder(Long id,
                                     Long userId,
                                     OrderStatus status,
                                     PaymentMethod paymentMethod,
                                     PaymentStatus payment,
                                     DeliveryStatus delivery,
                                     OrderVisibility visibility,
                                     String fullName,
                                     String orderUuid,
                                     String transactionId,
                                     Boolean removed) {
        JPAQuery<Order> contentQuery = queryFactory
                .select(order1)
                .from(order1)
                .where(order1.id.eq(id),
                       userIdEq(userId),
                       statusEq(status),
                       paymentMethodEq(paymentMethod),
                       paymentEq(payment),
                       deliverEq(delivery),
                       visibilityEq(visibility),
                       fullNameContains(fullName),
                       orderUuidContains(orderUuid),
                       transactionIdContains(transactionId),
                       removedEq(removed));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    BooleanExpression userIdEq(Long orderId) {
        return orderId != null ? order1.user.id.eq(orderId) : null;
    }

    BooleanExpression statusEq(OrderStatus status) {
        return status != null ? order1.status.eq(status) : null;
    }

    BooleanExpression paymentMethodEq(PaymentMethod paymentMethod) {
        return paymentMethod != null ? order1.paymentMethod.eq(paymentMethod) : null;
    }

    BooleanExpression paymentEq(PaymentStatus payment) {
        return payment != null ? order1.payment.eq(payment) : null;
    }

    BooleanExpression deliverEq(DeliveryStatus delivery) {
        return delivery != null ? order1.delivery.eq(delivery) : null;
    }

    BooleanExpression visibilityEq(OrderVisibility visibility) {
        return visibility != null ? order1.visible.eq(visibility) : null;
    }

    BooleanExpression fullNameContains(String fullName) {
        return fullName != null && !fullName.isBlank() ? order1.fullName.contains(fullName) : null;
    }

    BooleanExpression orderUuidContains(String orderUuid) {
        return orderUuid != null && !orderUuid.isBlank() ? order1.orderUuid.contains(orderUuid) : null;
    }

    BooleanExpression transactionIdContains(String transactionId) {
        return transactionId != null && !transactionId.isBlank() ? order1.transactionId.contains(transactionId) : null;
    }

    BooleanExpression removedEq(Boolean removed) {
        return removed != null ? order1.removed.eq(removed) : order1.removed.eq(false);
    }
}
