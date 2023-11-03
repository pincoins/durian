package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.VoucherCreateRequest;
import kr.pincoin.durian.shop.controller.dto.VoucherUpdateRequest;
import kr.pincoin.durian.shop.domain.Product;
import kr.pincoin.durian.shop.domain.Voucher;
import kr.pincoin.durian.shop.domain.conveter.ProductStatus;
import kr.pincoin.durian.shop.domain.conveter.VoucherStatus;
import kr.pincoin.durian.shop.repository.jpa.ProductRepository;
import kr.pincoin.durian.shop.repository.jpa.VoucherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class VoucherService {
    private final VoucherRepository voucherRepository;

    private final ProductRepository productRepository;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public List<Voucher>
    listVouchers(Long productId,
                 String code,
                 List<VoucherStatus> status,
                 Boolean removed) {
        return voucherRepository.findVouchers(productId, code, status, removed);
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Voucher>
    getVoucher(Long voucherId,
               Long productId,
               String code,
               List<VoucherStatus> status,
               Boolean removed) {
        return voucherRepository.findVoucher(voucherId, productId, code, status, removed);
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    @Transactional
    public Optional<Voucher>
    createVoucher(VoucherCreateRequest request) {
        Product product = productRepository.findProduct(request.getProductId(),
                                                        null,
                                                        null,
                                                        ProductStatus.ENABLED,
                                                        null,
                                                        false)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Invalid product",
                                                    List.of("Normal product does not exist.")));

        Voucher voucher = Voucher.builder(request.getCode(),
                                          request.getRemarks(),
                                          product).build();
        voucherRepository.save(voucher);

        return Optional.of(voucher);
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    @Transactional
    public Optional<Voucher>
    buyVoucher(Long voucherId) {
        Voucher voucher = voucherRepository.findVoucher(voucherId,
                                                        null,
                                                        null,
                                                        List.of(VoucherStatus.SOLD, VoucherStatus.REVOKED),
                                                        false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Voucher does not exist to mark as purchased.")));
        return Optional.of(voucher.purchased());
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    @Transactional
    public Optional<Voucher>
    sellVoucher(Long voucherId) {
        Voucher voucher = voucherRepository.findVoucher(voucherId,
                                                        null,
                                                        null,
                                                        List.of(VoucherStatus.PURCHASED),
                                                        false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Voucher does not exist to mark as sold.")));
        return Optional.of(voucher.sold());
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    @Transactional
    public Optional<Voucher>
    revokeVoucher(Long voucherId) {
        Voucher voucher = voucherRepository.findVoucher(voucherId,
                                                        null,
                                                        null,
                                                        List.of(VoucherStatus.PURCHASED, VoucherStatus.SOLD),
                                                        false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Voucher does not exist to mark as revoked.")));
        return Optional.of(voucher.revoked());
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    @Transactional
    public Optional<Voucher>
    updateVoucher(Long voucherId, VoucherUpdateRequest request) {
        Voucher voucher = voucherRepository.findVoucher(voucherId,
                                                        null,
                                                        null,
                                                        null,
                                                        false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Voucher does not exist to update.")));
        voucher.update(request);

        return Optional.of(voucher);
    }
}
