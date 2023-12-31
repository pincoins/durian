package kr.pincoin.durian.shop.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.*;
import kr.pincoin.durian.shop.domain.conveter.VoucherStatus;
import kr.pincoin.durian.shop.service.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vouchers")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping("")
    public ResponseEntity<List<VoucherResponse>>
    voucherList(@RequestParam(name = "productId", required = false) Long productId,
                @RequestParam(name = "code", required = false) String code,
                @RequestParam(name = "status", required = false) List<VoucherStatus> status,
                @RequestParam(name = "removed", required = false) Boolean removed) {
        return ResponseEntity
                .ok()
                .body(voucherService.listVouchers(productId, code, status, removed)
                              .stream()
                              .map(VoucherResponse::new)
                              .toList());
    }

    @GetMapping("/{voucherId}")
    public ResponseEntity<VoucherResponse>
    voucherDetail(@PathVariable Long voucherId,
                  @RequestParam(name = "productId", required = false) Long productId,
                  @RequestParam(name = "code", required = false) String code,
                  @RequestParam(name = "status", required = false) List<VoucherStatus> status,
                  @RequestParam(name = "removed", required = false) Boolean removed) {
        return voucherService.getVoucher(voucherId, productId, code, status, removed)
                .map(voucher -> ResponseEntity.ok().body(new VoucherResponse(voucher)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Voucher does not exist to retrieve.")));
    }

    @PostMapping("")
    public ResponseEntity<VoucherResponse>
    voucherCreate(@Valid @RequestBody VoucherCreateRequest request) {
        return voucherService.createVoucher(request)
                .map(voucher -> ResponseEntity.ok().body(new VoucherResponse(voucher)))
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT,
                                                    "Voucher creation failure",
                                                    List.of("Failed to create a new voucher.")));
    }

    @PutMapping("/{voucherId}/buy")
    public ResponseEntity<VoucherResponse>
    voucherBuy(@PathVariable Long voucherId) {
        return voucherService.buyVoucher(voucherId)
                .map(voucher -> ResponseEntity.ok().body(
                        new VoucherResponse(voucher)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Failed to mark as purchased.")));
    }

    @PutMapping("/{voucherId}/sell")
    public ResponseEntity<VoucherResponse>
    voucherSell(@PathVariable Long voucherId) {
        return voucherService.sellVoucher(voucherId)
                .map(voucher -> ResponseEntity.ok().body(
                        new VoucherResponse(voucher)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Failed to mark as sold.")));
    }

    @PutMapping("/{voucherId}/revoke")
    public ResponseEntity<VoucherResponse>
    voucherRevoke(@PathVariable Long voucherId) {
        return voucherService.revokeVoucher(voucherId)
                .map(voucher -> ResponseEntity.ok().body(
                        new VoucherResponse(voucher)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Failed to mark as revoked.")));
    }

    @PutMapping("/{voucherId}")
    public ResponseEntity<VoucherResponse>
    voucherUpdate(@PathVariable Long voucherId,
                  @Valid @RequestBody VoucherUpdateRequest request) {
        return voucherService.updateVoucher(voucherId, request)
                .map(voucher -> ResponseEntity.ok().body(
                        new VoucherResponse(voucher)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Failed to update voucher.")));
    }

    @PutMapping("/{voucherId}/products/{productId}")
    public ResponseEntity<VoucherResponse> voucherChangeProduct(@PathVariable Long voucherId,
                                                                @PathVariable Long productId) {
        return voucherService.changeProduct(voucherId, productId)
                .map(voucher -> ResponseEntity.ok().body(new VoucherResponse(voucher))).orElseThrow(
                        () -> new ApiException(HttpStatus.NOT_FOUND, "Voucher not found",
                                               List.of("Failed to change product of voucher.")));
    }

    @PutMapping("/{voucherId}/remove")
    public ResponseEntity<VoucherResponse>
    productRemove(@PathVariable Long voucherId) {
        return voucherService.remove(voucherId)
                .map(voucher -> ResponseEntity.ok().body(
                        new VoucherResponse(voucher)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Voucher not found",
                                                    List.of("Failed to remove voucher.")));
    }

    @PutMapping("/{voucherId}/restore")
    public ResponseEntity<VoucherResponse>
    productRestore(@PathVariable Long voucherId) {
        return voucherService.restore(voucherId)
                .map(voucher -> ResponseEntity.ok().body(
                        new VoucherResponse(voucher)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to restore voucher.")));
    }

    @DeleteMapping("/{voucherId}")
    public ResponseEntity<Void> voucherDelete(@PathVariable Long voucherId) {
        if (voucherService.deleteVoucher(voucherId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<Integer>
    voucherBulkCreate(@Valid @RequestBody VoucherBulkCreateRequest request) {
        return ResponseEntity.ok().body(voucherService.createVoucherBulk(request));
    }

    @PutMapping("/bulk-buy")
    public ResponseEntity<Integer>
    voucherBulkBuy(@Valid @RequestBody VoucherBulkRequest request) {
        return ResponseEntity.ok().body(voucherService.buyVoucherBulk(request));
    }

    @PutMapping("/bulk-sell")
    public ResponseEntity<Integer>
    voucherBulkSell(@Valid @RequestBody VoucherBulkRequest request) {
        return ResponseEntity.ok().body(voucherService.sellVoucherBulk(request));
    }

    @PutMapping("/bulk-revoke")
    public ResponseEntity<Integer>
    voucherRevoke(@Valid @RequestBody VoucherBulkRequest request) {
        return ResponseEntity.ok().body(voucherService.revokeVoucherBulk(request));
    }

    @PutMapping("/bulk-remove")
    public ResponseEntity<Integer>
    voucherBulkRemove(@Valid @RequestBody VoucherBulkRequest request) {
        return ResponseEntity.ok().body(voucherService.removeVoucherBulk(request));
    }

    @PutMapping("/bulk-restore")
    public ResponseEntity<Integer>
    voucherBulkRestore(@Valid @RequestBody VoucherBulkRequest request) {
        return ResponseEntity.ok().body(voucherService.restoreVoucherBulk(request));
    }

    @PutMapping("/bulk-change-product")
    public ResponseEntity<Integer>
    voucherBulkChangeProduct(@Valid @RequestBody VoucherBulkProductRequest request) {
        return ResponseEntity.ok().body(voucherService.changeProductBulk(request));
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<Integer>
    voucherBulkDelete(@Valid @RequestBody VoucherBulkRequest request) {
        return ResponseEntity.ok().body(voucherService.deleteVoucherBulk(request));
    }
}
