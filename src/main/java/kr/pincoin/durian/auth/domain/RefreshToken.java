package kr.pincoin.durian.auth.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 60)
public class RefreshToken {
    @Id // Redis: org.springframework.data.annotation.Id, JPA: javax.persistence.Id;
    private final String refreshToken;

    private final Long userId;

    public RefreshToken(final String refreshToken, final Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
