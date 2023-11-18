package kr.pincoin.durian.common.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleRecaptchaResult {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("challenge_ts")
    private LocalDateTime challengeTs;

    @JsonProperty("hostname")
    private String hostname;
}
