package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.Voucher;
import kr.pincoin.durian.shop.domain.conveter.VoucherStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.shop.domain.QVoucher.voucher;

@RequiredArgsConstructor
public class VoucherRepositoryImpl implements VoucherRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Voucher> findVouchers(Long productId,
                                      String code,
                                      List<VoucherStatus> status,
                                      Boolean removed) {
        JPAQuery<Voucher> contentQuery = queryFactory
                .select(voucher)
                .from(voucher)
                .innerJoin(voucher.product)
                .fetchJoin()
                .where(productIdEq(productId),
                       codeContains(code),
                       statusIn(status),
                       removedEq(removed));

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
