package kr.pincoin.durian.shop.domain.conveter;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PAYMENT_PENDING,
    PAYMENT_COMPLETED,
    UNDER_REVIEW,
    PAYMENT_VERIFIED,
    SHIPPED,
    REFUND_REQUESTED,
    REFUND_PENDING,
    REFUNDED1,
    REFUNDED2,
    VOIDED
}
