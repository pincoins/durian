package kr.pincoin.durian.shop.domain.conveter;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    BANK_TRANSFER,
    ESCROW,
    PAYPAL,
    CREDIT_CARD,
    PHONE_BILL,
}
