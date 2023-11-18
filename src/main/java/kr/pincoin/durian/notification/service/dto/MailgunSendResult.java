package kr.pincoin.durian.notification.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailgunSendResult {
    @JsonProperty("id")
    private String id;

    @JsonProperty("message")
    private String message;
}
