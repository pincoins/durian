package kr.pincoin.durian.auth.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "refreshToken")
public class RefreshToken {
    @Value("${auth.jwt.refresh-token-expires-in}")
    private int jwtRefreshTokenExpiresIn;

    @Id
    // Redis: org.springframework.data.annotation.Id
    // JPA: javax.persistence.Id;
    // findById (not findByRefreshToken)
    private final String refreshToken;

    private final Long userId;

    private final String ipAddress;

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
    }

    @TimeToLive
    public Long getTimeToLive() {
        return (long) jwtRefreshTokenExpiresIn;
    }
}
