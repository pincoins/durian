package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.controller.dto.VoucherBulkCreateRequest;
import kr.pincoin.durian.shop.controller.dto.VoucherNested;
import kr.pincoin.durian.shop.domain.Voucher;
import kr.pincoin.durian.shop.domain.conveter.VoucherStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.shop.domain.QVoucher.voucher;

@RequiredArgsConstructor
public class VoucherRepositoryImpl implements VoucherRepositoryQuery {
    private static final int BATCH_SIZE = 500;

    private final JPAQueryFactory queryFactory;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int saveAll(VoucherBulkCreateRequest request) {
        Long productId = request.getProductId();

        List<VoucherNested> vouchers = new ArrayList<>();

        for (int i = 0; i < request.getVouchers().size(); i++) {
            vouchers.add(request.getVouchers().get(i));
            if ((i + 1) % BATCH_SIZE == 0) {
                batchInsert(productId, vouchers);
            }
        }

        if (!vouchers.isEmpty()) {
            batchInsert(productId, vouchers);
        }

        return request.getVouchers().size();
    }

    private void batchInsert(Long productId, List<VoucherNested> vouchers) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO shop_voucher (code, remarks, product_id, status, is_removed, created, modified)" +
                        " VALUES (?, ?, ?, 'PURCHASED', 0, NOW(), NOW())",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, vouchers.get(i).getCode());
                        ps.setString(2, vouchers.get(i).getRemarks());
                        ps.setLong(3, productId);
                    }

                    @Override
                    public int getBatchSize() {
                        return vouchers.size();
                    }
                });

        vouchers.clear();
    }

    @Override
    public List<Voucher> findVouchers(Long productId,
                                      String code,
                                      List<VoucherStatus> status,
                                      Boolean removed,
                                      Integer limit) {
        JPAQuery<Voucher> contentQuery = queryFactory
                .select(voucher)
                .from(voucher)
                .innerJoin(voucher.product)
                .fetchJoin()
                .where(productIdEq(productId),
                       codeContains(code),
                       statusIn(status),
                       removedEq(removed))
                .orderBy(voucher.id.asc());

        if (limit != null) {
            contentQuery.limit(limit);
        }

        return contentQuery.fetch();
    }

    @Override
    public Optional<Voucher> findVoucher(Long voucherId,
                                         Long productId,
                                         String code,
                                         List<VoucherStatus> status,
                                         Boolean removed) {
        JPAQuery<Voucher> contentQuery = queryFactory
                .select(voucher)
                .from(voucher)
                .innerJoin(voucher.product)
                .fetchJoin()
                .where(voucher.id.eq(voucherId),
                       productIdEq(productId),
                       codeContains(code),
                       statusIn(status),
                       removedEq(removed));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    BooleanExpression productIdEq(Long productId) {
        return productId != null ? voucher.product.id.eq(productId) : null;
    }

    BooleanExpression codeContains(String code) {
        return code != null && !code.isBlank() ? voucher.code.contains(code) : null;
    }

    BooleanExpression statusIn(List<VoucherStatus> status) {
        return status != null ? voucher.status.in(status) : null;
    }

    BooleanExpression removedEq(Boolean removed) {
        return removed != null ? voucher.removed.eq(removed) : voucher.removed.eq(false);
    }
}
