package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.OrderItemVoucher;
import kr.pincoin.durian.shop.domain.conveter.OrderStatus;
import kr.pincoin.durian.shop.domain.conveter.OrderVisibility;
import kr.pincoin.durian.shop.repository.jpa.dto.OrderItemVoucherDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static kr.pincoin.durian.shop.domain.QOrder.order1;
import static kr.pincoin.durian.shop.domain.QOrderItem.orderItem;
import static kr.pincoin.durian.shop.domain.QOrderItemVoucher.orderItemVoucher;

@RequiredArgsConstructor
public class OrderItemVoucherRepositoryImpl implements OrderItemVoucherRepositoryQuery {
    private static final int BATCH_SIZE = 500;

    private final JPAQueryFactory queryFactory;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int saveAll(List<OrderItemVoucherDto> orderItemVouchers) {
        List<OrderItemVoucherDto> vouchers = new ArrayList<>();

        for (int i = 0; i < orderItemVouchers.size(); i++) {
            vouchers.add(orderItemVouchers.get(i));
            if ((i + 1) % BATCH_SIZE == 0) {
                batchInsert(vouchers);
            }
        }

        if (!vouchers.isEmpty()) {
            batchInsert(vouchers);
        }

        return orderItemVouchers.size();
    }

    private void batchInsert(List<OrderItemVoucherDto> vouchers) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO shop_order_item_voucher (code, remarks, order_item_id, voucher_id, revoked, created, modified)" +
                        " VALUES (?, ?, ?, ?, 0, NOW(), NOW())",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, vouchers.get(i).getCode());
                        ps.setString(2, vouchers.get(i).getRemarks());
                        ps.setLong(3, vouchers.get(i).getOrderItemId());
                        ps.setLong(4, vouchers.get(i).getVoucherId());
                    }

                    @Override
                    public int getBatchSize() {
                        return vouchers.size();
                    }
                });

        vouchers.clear();
    }

    @Override
    public List<OrderItemVoucher> findOrderItemVouchers(Long orderId,
                                                        Long userId,
                                                        Long itemId,
                                                        OrderStatus status,
                                                        OrderVisibility visibility,
                                                        Boolean removed) {
        JPAQuery<OrderItemVoucher> contentQuery = queryFactory
                .select(orderItemVoucher)
                .from(orderItemVoucher)
                .innerJoin(orderItemVoucher.orderItem, orderItem)
                .innerJoin(orderItem.order, order1)
                .where(orderItemVoucher.orderItem.order.id.eq(orderId),
                       userIdEq(userId),
                       itemIdEq(itemId),
                       statusEq(status),
                       visibilityEq(visibility),
                       removedEq(removed));

        return contentQuery.fetch();
    }

    BooleanExpression userIdEq(Long orderId) {
        return orderId != null ? order1.user.id.eq(orderId) : null;
    }

    BooleanExpression itemIdEq(Long itemEq) {
        return itemEq != null ? orderItem.id.eq(itemEq) : null;
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
