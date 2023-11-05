package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.OrderItem;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
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
                                          OrderStatus status,
                                          OrderVisibility visibility,
                                          Boolean removed) {
        JPAQuery<OrderItem> contentQuery = queryFactory
                .select(orderItem)
                .from(orderItem)
                .innerJoin(orderItem.order, order1)
                .fetchJoin()
                .where(order1.id.eq(orderId),
                       userIdEq(userId),
                       statusEq(status),
                       visibilityEq(visibility),
                       removedEq(removed));

        return contentQuery.fetch();
    }

    BooleanExpression userIdEq(Long orderId) {
        return orderId != null ? order1.user.id.eq(orderId) : null;
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
