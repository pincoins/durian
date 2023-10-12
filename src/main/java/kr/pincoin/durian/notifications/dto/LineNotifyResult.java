package kr.pincoin.durian.notifications.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineNotifyResult {
    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    public LineNotifyResult(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
