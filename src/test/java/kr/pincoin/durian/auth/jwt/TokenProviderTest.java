package kr.pincoin.durian.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static kr.pincoin.durian.auth.util.jwt.TokenProvider.ACCESS_TOKEN_EXPIRES_IN;
import static org.assertj.core.api.Assertions.assertThat;

public class TokenProviderTest {

    @Test
    void tokenTest() {
        String signKey = "2f248da39cace10ec3be053d47b3afaa9adc637d831f472ec38434818bb7510b8b7573dcee8f7d8aa4d89c008c1de072";

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(signKey));

        String sub = "1";
        String username = "John";

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS512");

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        String jwt = Jwts.builder()
                .header().add(headers)
                .and()
                .claims(claims)
                .expiration(Date.from(LocalDateTime.now()
                                              .plus(Duration.of(ACCESS_TOKEN_EXPIRES_IN, ChronoUnit.SECONDS))
                                              .atZone(ZoneId.systemDefault()).toInstant())) // exp
                // .id(jti)
                .subject(sub)
                .signWith(key)
                .compact();

        Jws<Claims> jws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt);

        Claims payloads = jws.getPayload();

        assertThat(payloads.get("username")).isEqualTo(username);
        assertThat(payloads.getSubject()).isEqualTo(sub);
    }
}
