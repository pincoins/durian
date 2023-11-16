package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExistenceResponse {
    @JsonProperty("exists")
    private boolean exists;

    public ExistenceResponse(boolean exists) {
        this.exists = exists;
    }
}
