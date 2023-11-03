package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoucherCreateRequest {
    @JsonProperty("code")
    @NotEmpty
    @NotBlank
    private String code;

    @JsonProperty("remarks")
    @NotEmpty
    @NotBlank
    private String remarks;

    @JsonProperty("productId")
    @NotNull
    private Long productId;
}
