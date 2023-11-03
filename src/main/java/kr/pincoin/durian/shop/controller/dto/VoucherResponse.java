package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.Voucher;
import kr.pincoin.durian.shop.domain.conveter.VoucherStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoucherResponse {
    @JsonProperty("voucherId")
    private Long id;

    @JsonProperty("removed")
    private boolean removed;

    @JsonProperty("code")
    private String code;

    @JsonProperty("remarks")
    private String remarks;

    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("status")
    private VoucherStatus status;

    public VoucherResponse(Voucher voucher) {
        this.id = voucher.getId();
        this.removed = voucher.isRemoved();
        this.code = voucher.getCode();
        this.remarks = voucher.getRemarks();
        this.productId = voucher.getProduct().getId();
        this.status = voucher.getStatus();
    }
}
