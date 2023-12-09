package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.Order;
import kr.pincoin.durian.shop.domain.conveter.*;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.auth.domain.QUser.user;
import static kr.pincoin.durian.shop.domain.QOrder.order1;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Order> findOrders(Long userId,
                                  OrderStatus status,
                                  PaymentMethod paymentMethod,
                                  PaymentStatus payment,
                                  SendingStatus sending,
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
                       sendingEq(sending),
                       visibilityEq(visibility),
                       fullNameContains(fullName),
                       orderUuidContains(orderUuid),
                       transactionIdContains(transactionId),
                       removedEq(removed));

        return contentQuery.fetch();
    }

    @Override
    public List<Order> findOrders(OrderStatus status,
                                  PaymentMethod paymentMethod,
                                  PaymentStatus payment,
                                  SendingStatus sending,
                                  OrderVisibility visibility,
                                  String fullName,
                                  String orderUuid,
                                  String transactionId,
                                  Boolean removed) {
        JPAQuery<Order> contentQuery = queryFactory
                .select(order1)
                .from(order1)
                .innerJoin(order1.user)
                .fetchJoin()
                .innerJoin(user.profiles)
                .fetchJoin()
                .where(statusEq(status),
                       paymentMethodEq(paymentMethod),
                       paymentEq(payment),
                       sendingEq(sending),
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
                                     SendingStatus sending,
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
                       sendingEq(sending),
                       visibilityEq(visibility),
                       fullNameContains(fullName),
                       orderUuidContains(orderUuid),
                       transactionIdContains(transactionId),
                       removedEq(removed));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public Optional<Order> findOrderWithPayments(Long id,
                                                 Long userId,
                                                 OrderStatus status,
                                                 PaymentMethod paymentMethod,
                                                 PaymentStatus payment,
                                                 SendingStatus sending,
                                                 OrderVisibility visibility,
                                                 String fullName,
                                                 String orderUuid,
                                                 String transactionId,
                                                 Boolean removed) {
        JPAQuery<Order> contentQuery = queryFactory
                .select(order1)
                .from(order1)
                .innerJoin(order1.payments)
                .fetchJoin()
                .where(order1.id.eq(id),
                       userIdEq(userId),
                       statusEq(status),
                       paymentMethodEq(paymentMethod),
                       paymentEq(payment),
                       sendingEq(sending),
                       visibilityEq(visibility),
                       fullNameContains(fullName),
                       orderUuidContains(orderUuid),
                       transactionIdContains(transactionId),
                       removedEq(removed));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    @Override
    public List<Order> findDistinctOrderWithItems(Long id,
                                                  Long userId,
                                                  OrderStatus status,
                                                  PaymentMethod paymentMethod,
                                                  PaymentStatus payment,
                                                  SendingStatus sending,
                                                  OrderVisibility visibility,
                                                  String fullName,
                                                  BigDecimal totalSellingPrice,
                                                  String orderUuid,
                                                  String transactionId,
                                                  Boolean removed) {
        JPAQuery<Order> contentQuery = queryFactory
                .select(order1)
                .distinct()
                .from(order1)
                .innerJoin(order1.user, user)
                .fetchJoin()
                .innerJoin(order1.items)
                .fetchJoin()
                .where(order1.id.eq(id),
                       userIdEq(userId),
                       statusEq(status),
                       paymentMethodEq(paymentMethod),
                       paymentEq(payment),
                       sendingEq(sending),
                       visibilityEq(visibility),
                       fullNameContains(fullName),
                       totalSellingPriceLoe(totalSellingPrice),
                       orderUuidContains(orderUuid),
                       transactionIdContains(transactionId),
                       removedEq(removed));

        return contentQuery.fetch();
    }

    BooleanExpression userIdEq(Long userId) {
        return userId != null ? order1.user.id.eq(userId) : null;
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

    BooleanExpression sendingEq(SendingStatus sending) {
        return sending != null ? order1.sending.eq(sending) : null;
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

    BooleanExpression totalSellingPriceLoe(BigDecimal totalSellingPrice) {
        return totalSellingPrice != null ? order1.totalSellingPrice.loe(totalSellingPrice) : null;
    }

    BooleanExpression removedEq(Boolean removed) {
        return removed != null ? order1.removed.eq(removed) : order1.removed.eq(false);
    }
}
