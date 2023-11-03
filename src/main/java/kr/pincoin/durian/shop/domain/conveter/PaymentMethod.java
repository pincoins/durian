package kr.pincoin.durian.shop.domain.conveter;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    BANK_TRANSFER,
    ESCROW,
    PAYPAL,
    CREDIT_CARD,
    BANK_TRANSFER_PG,
    VIRTUAL_ACCOUNT,
    PHONE_BILL,
}
