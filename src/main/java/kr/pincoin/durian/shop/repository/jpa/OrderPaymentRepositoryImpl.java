package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.OrderPayment;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.pincoin.durian.shop.domain.QOrder.order1;
import static kr.pincoin.durian.shop.domain.QOrderPayment.orderPayment;

@RequiredArgsConstructor
public class OrderPaymentRepositoryImpl implements OrderPaymentRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderPayment> findOrderPayments(Long orderId,
                                                Long userId,
                                                OrderStatus status,
                                                OrderVisibility visibility,
                                                Boolean removed) {
        JPAQuery<OrderPayment> contentQuery = queryFactory
                .select(orderPayment)
                .from(orderPayment)
                .innerJoin(orderPayment.order, order1)
                .fetchJoin()
                .where(order1.id.eq(orderId),
                       userIdEq(userId),
                       statusEq(status),
                       visibilityEq(visibility),
                       removedEq(removed));

        return contentQuery.fetch();
    }

    BooleanExpression userIdEq(Long userId) {
        return userId != null ? order1.user.id.eq(userId) : null;
    }

    BooleanExpression statusEq(OrderStatus status) {
        return status != null ? order1.status.eq(status) : null;
    }

    BooleanExpression visibilityEq(OrderVisibility visibility) {
        return visibility != null ? order1.visible.eq(visibility) : null;
    }

    BooleanExpression removedEq(Boolean removed) {
        return removed != null ? order1.removed.eq(removed) : order1.removed.eq(false);
    }
}
