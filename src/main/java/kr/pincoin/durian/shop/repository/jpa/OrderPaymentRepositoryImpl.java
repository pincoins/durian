package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.OrderPayment;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.pincoin.durian.shop.domain.QOrder.order1;
import static kr.pincoin.durian.shop.domain.QOrderPayment.orderPayment;

@RequiredArgsConstructor
public class OrderPaymentRepositoryImpl implements OrderPaymentRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderPayment> findOrderPayments(Long orderId,
                                                Long userId) {
        JPAQuery<OrderPayment> contentQuery = queryFactory
                .select(orderPayment)
                .from(orderPayment)
                .innerJoin(orderPayment.order, order1)
                .fetchJoin()
                .where(order1.id.eq(orderId),
                       userIdEq(userId));

        return contentQuery.fetch();
    }

    BooleanExpression userIdEq(Long orderId) {
        return orderId != null ? order1.user.id.eq(orderId) : null;
    }
}
