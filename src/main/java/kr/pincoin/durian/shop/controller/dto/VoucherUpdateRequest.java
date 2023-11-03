package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.common.validation.NullOrNotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoucherUpdateRequest {
    @JsonProperty("code")
    @NullOrNotBlank
    private String code;

    @JsonProperty("remarks")
    @NullOrNotBlank
    private String remarks;
}
