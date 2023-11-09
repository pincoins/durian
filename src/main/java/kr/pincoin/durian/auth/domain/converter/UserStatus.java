package kr.pincoin.durian.auth.domain.converter;

import lombok.Getter;

@Getter
public enum UserStatus {
    PENDING,
    NORMAL,
    INACTIVE,
    UNREGISTERED,
}
