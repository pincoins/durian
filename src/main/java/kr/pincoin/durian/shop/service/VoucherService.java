package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.VoucherBulkCreateRequest;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return Optional.of(voucher.update(request));
    }

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    @Transactional
    public Optional<Voucher>
    changeProduct(Long voucherId, Long productId) {
        Product product = productRepository
                .findProduct(productId,
                             null,
                             null,
                             ProductStatus.ENABLED,
                             null,
                             false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Normal product not found",
                                                    List.of("Product does not exist for voucher to change.")));
        Voucher voucher = voucherRepository
                .findVoucher(voucherId,
                             null,
                             null,
                             null,
                             false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Voucher does not exist to change product.")));

        return Optional.of(voucher.changeProduct(product));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Voucher> remove(Long voucherId) {
        Voucher voucher = voucherRepository
                .findVoucher(voucherId,
                             null,
                             null,
                             null,
                             false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Voucher does not exist to remove.")));

        return Optional.of(voucher.remove());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Voucher> restore(Long voucherId) {
        Voucher voucher = voucherRepository
                .findVoucher(voucherId,
                             null,
                             null,
                             null,
                             true)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Voucher does not exist to restore.")));

        return Optional.of(voucher.restore());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public boolean deleteVoucher(Long voucherId) {
        return voucherRepository.findVoucher(voucherId,
                                             null,
                                             null,
                                             null,
                                             true)
                .map(voucher -> {
                    voucherRepository.disconnect(voucher); // set order_item_voucher.voucher_id = null
                    voucherRepository.delete(voucher);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Soft removed voucher not found",
                                                      List.of("Voucher does not exist to delete.")));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Integer
    createVoucherBulk(VoucherBulkCreateRequest request) {
        try {
            return voucherRepository.saveAll(request);
        } catch (DataIntegrityViolationException ex) {
            Matcher matcher = Pattern.compile("Duplicate entry '([a-zA-Z0-9-_]+)'").matcher(ex.getLocalizedMessage());

            String message = "Duplicate voucher: ";

            if (matcher.find()) {
                message = message + matcher.group(1).trim();
            }

            throw new ApiException(HttpStatus.CONFLICT,
                                   "Duplicate voucher code",
                                   List.of(message),
                                   ex);
        }
    }
}
