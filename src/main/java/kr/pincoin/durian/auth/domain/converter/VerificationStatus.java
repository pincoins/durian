package kr.pincoin.durian.auth.domain.converter;

import lombok.Getter;

@Getter
public enum VerificationStatus {
    UNVERIFIED,
    VERIFIED,
    REVOKED
}
