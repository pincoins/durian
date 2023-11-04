package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static kr.pincoin.durian.auth.controller.dto.UserCreateRequest.USERNAME_PATTERN;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChangeUsernameRequest {
    @JsonProperty("username")
    @Pattern(regexp = USERNAME_PATTERN)
    private String username;
}
