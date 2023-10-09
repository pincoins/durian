package kr.pincoin.durian.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenRequest {
    // https://www.oauth.com/oauth2-servers/making-authenticated-requests/refreshing-an-access-token/

    @JsonProperty("grantType")
    @NotNull
    @Pattern(regexp = "refresh_token", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid grant type")
    private String grantType;

    @JsonProperty("refreshToken")
    @NotNull
    @NotBlank
    private String refreshToken;

    private String scope;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("clientSecret")
    private String clientSecret;

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
