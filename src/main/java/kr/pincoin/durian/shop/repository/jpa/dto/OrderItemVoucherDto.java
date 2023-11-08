package kr.pincoin.durian.shop.repository.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemVoucherDto {
    private String code;

    private String remarks;

    private boolean revoked;

    private Long orderItemId;

    private Long voucherId;
}
