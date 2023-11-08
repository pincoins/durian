package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.OrderItem;
import kr.pincoin.durian.shop.domain.conveter.*;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemCountResult;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemProductResult;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.pincoin.durian.shop.domain.QOrder.order1;
import static kr.pincoin.durian.shop.domain.QOrderItem.orderItem;
import static kr.pincoin.durian.shop.domain.QOrderItemVoucher.orderItemVoucher;
import static kr.pincoin.durian.shop.domain.QProduct.product;
import static kr.pincoin.durian.shop.domain.QVoucher.voucher;

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

    @Override
    public List<OrderItem> findOrderItemsWithVouchers(Long orderId,
                                                      Long userId,
                                                      OrderStatus status,
                                                      OrderVisibility visibility,
                                                      Boolean removed) {
        JPAQuery<OrderItem> contentQuery = queryFactory
                .select(orderItem)
                .from(orderItem)
                .innerJoin(orderItem.order, order1)
                .innerJoin(orderItem.vouchers)
                .where(orderItem.order.id.eq(orderId),
                       userIdEq(userId),
                       statusEq(status),
                       visibilityEq(visibility),
                       removedEq(removed));

        return contentQuery.fetch();
    }

    @Override
    public List<OrderItemProductResult> findOrderItemWithProduct(Long orderId,
                                                                 Long userId) {
        JPAQuery<OrderItemProductResult> contentQuery = queryFactory
                .select(Projections.fields(OrderItemProductResult.class,
                                           orderItem,
                                           product,
                                           voucher.id.count().as("remaining")))
                .from(voucher)
                .innerJoin(voucher.product, product)
                .innerJoin(orderItem)
                .on(product.slug.eq(orderItem.slug))
                .where(orderItem.order.id.eq(orderId),
                       voucher.status.eq(VoucherStatus.PURCHASED),
                       product.removed.eq(false),
                       product.status.eq(ProductStatus.ENABLED),
                       product.stock.eq(ProductStockStatus.IN_STOCK))
                .groupBy(product.id);

        return contentQuery.fetch();
    }

    @Override
    public List<OrderItemCountResult> findVoucherCountByOrderItem(Long orderId,
                                                                  Long userId) {
        JPAQuery<OrderItemCountResult> contentQuery = queryFactory
                .select(Projections.fields(OrderItemCountResult.class,
                                           orderItem,
                                           orderItem.id.count().as("count")))
                .from(orderItem)
                .innerJoin(orderItem.vouchers, orderItemVoucher)
                .where(orderItemVoucher.revoked.eq(false))
                .groupBy(orderItem.id);

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
