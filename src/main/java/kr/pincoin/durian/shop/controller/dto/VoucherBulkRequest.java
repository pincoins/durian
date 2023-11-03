package kr.pincoin.durian.shop.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoucherBulkRequest {
    @NotEmpty
    private List<Long> voucherIds = new ArrayList<>();
}
