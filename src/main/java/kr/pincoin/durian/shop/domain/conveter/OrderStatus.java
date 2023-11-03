package kr.pincoin.durian.shop.domain.conveter;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDERED,
    CANCELED,
    VOIDED,
}
