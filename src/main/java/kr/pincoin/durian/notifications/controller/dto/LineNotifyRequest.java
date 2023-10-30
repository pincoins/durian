package kr.pincoin.durian.notifications.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineNotifyRequest {
    @JsonProperty("message")
    private String message;
}
