package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.OrderItemVoucher;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemVoucherResponse {
    @JsonProperty("code")
    private String code;

    @JsonProperty("remarks")
    private String remarks;

    @JsonProperty("revoked")
    private boolean revoked;

    public OrderItemVoucherResponse(OrderItemVoucher orderItemVoucher) {
        this.code = orderItemVoucher.getCode();
        this.remarks = orderItemVoucher.getRemarks();
        this.revoked = orderItemVoucher.isRevoked();
    }
}
