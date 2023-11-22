package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationResponse {
    @JsonProperty("success")
    @NotNull
    private boolean success;

    public EmailVerificationResponse(boolean success) {
        this.success = success;
    }
}
