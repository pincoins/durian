package kr.pincoin.durian.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "emailVerification")
@RequiredArgsConstructor
@Slf4j
public class EmailVerification {
    @Id
    // Redis: org.springframework.data.annotation.Id
    // JPA: javax.persistence.Id;
    // findById (not findByIpAddress)
    private final String ipAddressAndEmail;

    private final String userAgent;

    private final String code;

    @TimeToLive
    private Long timeout;

    public EmailVerification setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }
}
