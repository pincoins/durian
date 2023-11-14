package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessTokenResponse {
    // https://www.oauth.com/oauth2-servers/access-tokens/access-token-response/

    @JsonProperty("accessToken")
    @NotNull
    @NotBlank
    private String accessToken;

    @JsonProperty("tokenType")
    @NotNull
    @NotBlank
    private String tokenType;

    @JsonProperty("expiresIn")
    @NotNull
    private Integer expiresIn;

    @JsonIgnore
    private String refreshToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String scope;

    public AccessTokenResponse(String accessToken,
                               Integer expiresIn) {
        this.tokenType = "Bearer";
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public AccessTokenResponse(String accessToken,
                               Integer expiresIn,
                               String refreshToken) {
        this(accessToken, expiresIn);
        this.refreshToken = refreshToken;
    }
}
