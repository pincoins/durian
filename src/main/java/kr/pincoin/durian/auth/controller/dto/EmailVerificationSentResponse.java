package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationSentResponse {
    @JsonProperty("success")
    @NotNull
    private boolean success;

    public EmailVerificationSentResponse(boolean success) {
        this.success = success;
    }
}
