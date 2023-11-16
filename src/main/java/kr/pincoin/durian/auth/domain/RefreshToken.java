package kr.pincoin.durian.auth.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "refreshToken")
@Slf4j
public class RefreshToken {
    @Id
    // Redis: org.springframework.data.annotation.Id
    // JPA: javax.persistence.Id;
    // findById (not findByRefreshToken)
    private final String refreshToken;

    private final Long userId;

    private final String ipAddress;

    @TimeToLive
    private Long timeout;

    // Select
    // > HGETALL "refreshToken:0593ff9f-f43a-4081-8461-540b874e2477"
    // 1) "_class"
    // 2) "kr.pincoin.durian.auth.domain.RefreshToken"
    // 3) "ipAddress"
    // 4) "0:0:0:0:0:0:0:1"
    // 5) "refreshToken"
    // 6) "0593ff9f-f43a-4081-8461-540b874e2477"
    // 7) "userId"
    // 8) "30"

    // TTL: -2 if the key does not exist. / -1 if the key exists but has no associated expire.
    // > TTL "refreshToken:0593ff9f-f43a-4081-8461-540b874e2477"
    //(integer) -1

    public RefreshToken(final String refreshToken, final Long userId, final String ipAddress) {
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.ipAddress = ipAddress;

        log.warn(ipAddress);
    }

    public RefreshToken setTimeout(Long timeout) {
        // It must be set in setter instead of constructor.
        this.timeout = timeout;
        return this;
    }
}
