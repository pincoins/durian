package kr.pincoin.durian.shop.domain.conveter;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    UNPAID,
    PAID,
    PARTIALLY_PAID,
    REFUND_REQUESTED,
    NOT_REFUNDED,
    REFUNDED,
}
