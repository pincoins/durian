package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoucherBulkCreateRequest {
    @JsonProperty("productId")
    @NotNull
    private Long productId;

    @JsonProperty("vouchers")
    @NotNull
    private List<VoucherRecord> vouchers = new ArrayList<>();


}
