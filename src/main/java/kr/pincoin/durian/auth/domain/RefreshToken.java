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

    public RefreshToken(final String refreshToken, final Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    @TimeToLive
    public Long getTimeToLive() {
        return (long) jwtRefreshTokenExpiresIn;
    }
}
