package kr.pincoin.durian.auth.domain.converter;

import lombok.Getter;

@Getter
public enum PhoneVerifiedStatus {
    UNVERIFIED,
    VERIFIED,
    REVOKED
}
