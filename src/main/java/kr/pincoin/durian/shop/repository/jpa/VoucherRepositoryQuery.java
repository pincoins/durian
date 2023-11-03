package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.controller.dto.VoucherBulkCreateRequest;
import kr.pincoin.durian.shop.domain.Voucher;
import kr.pincoin.durian.shop.domain.conveter.VoucherStatus;

import java.util.List;
import java.util.Optional;

public interface VoucherRepositoryQuery {
    int saveAll(VoucherBulkCreateRequest request);

    List<Voucher> findVouchers(Long productId,
                               String code,
                               List<VoucherStatus> status,
                               Boolean removed);

    Optional<Voucher> findVoucher(Long voucherId,
                                  Long productId,
                                  String code,
                                  List<VoucherStatus> status,
                                  Boolean removed);
}
