package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.OrderItem;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.pincoin.durian.shop.domain.QOrder.order1;
import static kr.pincoin.durian.shop.domain.QOrderItem.orderItem;

@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderItem> findOrderItems(Long orderId,
                                          Long userId,
                                          Boolean orderRemoved,
                                          Boolean removed) {
        JPAQuery<OrderItem> contentQuery = queryFactory
                .select(orderItem)
                .from(orderItem)
                .innerJoin(orderItem.order, order1)
                .fetchJoin()
                .where(order1.id.eq(orderId),
                       userIdEq(userId),
                       orderRemovedEq(orderRemoved),
                       removedEq(removed));

        return contentQuery.fetch();
    }

    BooleanExpression removedEq(Boolean removed) {
        return removed != null ? orderItem.removed.eq(removed) : orderItem.removed.eq(false);
    }

    BooleanExpression orderRemovedEq(Boolean orderRemoved) {
        return orderRemoved != null ? order1.removed.eq(orderRemoved) : order1.removed.eq(false);
    }

    BooleanExpression userIdEq(Long orderId) {
        return orderId != null ? order1.user.id.eq(orderId) : null;
    }
}
