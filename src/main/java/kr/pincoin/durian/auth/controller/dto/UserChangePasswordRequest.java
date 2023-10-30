package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static kr.pincoin.durian.auth.controller.dto.UserCreateRequest.PASSWORD_PATTERN;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChangePasswordRequest {
    @JsonProperty("userId")
    @NotNull
    private Long userId;

    @JsonProperty("oldPassword")
    @Size(min = 8, max = 32)
    @Pattern(regexp = PASSWORD_PATTERN)
    private String oldPassword;

    @JsonProperty("newPassword")
    @Size(min = 8, max = 32)
    @Pattern(regexp = PASSWORD_PATTERN)
    private String newPassword;
}
