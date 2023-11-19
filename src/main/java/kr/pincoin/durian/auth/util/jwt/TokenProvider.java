package kr.pincoin.durian.auth.util.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import kr.pincoin.durian.auth.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Component
public class TokenProvider {
    public static final String ERROR_401_INVALID_SECRET_KEY = "2001";

    public static final String ERROR_401_EXPIRED_JWT = "2002";

    public static final String ERROR_401_INVALID_TOKEN = "2003";

    // HS256: openssl rand -hex 24
    // HS384: openssl rand -hex 32
    // HS512: openssl rand -hex 48
    @Value("${auth.jwt.secret-key}")
    private String jwtSecretSignKey;

    @Value("${auth.jwt.access-token-expires-in}")
    private int jwtAccessTokenExpiresIn;

    public static final String JWT_TYPE = "JWT";

    public static final String JWT_ALGORITHM = "HS512";

    public String
    getXAuthToken(HttpServletRequest request) {
        // Header format
        // Non-standard header
        // X-Auth-Token : JWTString=
        final String header = request.getHeader("X-AUTH-TOKEN");

        if (header != null && !header.isBlank()) {
            return header;
        }
        return null;
    }

    public String
    getBearerToken(HttpServletRequest request) {
        // Header format
        // RFC 7235 standard header
        // Authorization: Bearer JWTString=
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            return header.split(" ")[1].trim();
        }

        return null;
    }

    public Optional<String>
    validateAccessToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretSignKey));

        try {
            Jws<Claims> jws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

            return Optional.ofNullable(jws.getPayload().getSubject());
        } catch (SignatureException | DecodingException ex) {
            throw new JwtException(ERROR_401_INVALID_SECRET_KEY, "Invalid secret key", ex);
        } catch (ExpiredJwtException ex) {
            throw new JwtException(ERROR_401_EXPIRED_JWT, "Expired token", ex);
        } catch (UnsupportedJwtException | MalformedJwtException | SecurityException | IllegalArgumentException ex) {
            throw new JwtException(ERROR_401_INVALID_TOKEN, "Invalid token format", ex);
        }
    }

    public String
    createAccessToken(User user) {
        // access token can contain personal information such as username
        // access token is not saved in RDBMS or NoSQL

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretSignKey));

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", JWT_TYPE);
        headers.put("alg", JWT_ALGORITHM);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());

        return Jwts.builder()
                .header().add(headers)
                .and()
                .claims(claims)
                .expiration(Date.from(LocalDateTime.now()
                                              .plus(Duration.of(jwtAccessTokenExpiresIn,
                                                                ChronoUnit.SECONDS)) // 60 minutes
                                              .atZone(ZoneId.systemDefault()).toInstant())) // exp
                .subject(String.valueOf(user.getId())) // sub
                .signWith(key)
                .compact();
    }

    public String
    createRefreshToken() {
        // refresh token does not have personal information such as username
        // refresh token can be saved in NoSQL or RDBMS

        return UUID.randomUUID().toString();
    }
}
