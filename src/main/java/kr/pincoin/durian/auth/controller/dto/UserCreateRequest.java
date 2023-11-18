package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCreateRequest {
    // Uppercase letter at least one or more (?=.*?[A-Z])
    // Lowercase letter at least one or more (?=.*?[a-z])
    // Digit at least one or more (?=.*?[0-9])
    // Special character at least one ore more (?=.*?[#?!@$%^&*-])
    // 8 characters or more .{8,} (with the anchors)
    // Django: https://github.com/django/django/blob/main/django/contrib/auth/password_validation.py
    public static final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    @JsonProperty("username")
    @NotEmpty
    @NotBlank
    @Email
    private String username;

    @JsonProperty("fullName")
    @NotEmpty
    @NotBlank
    @Size(min = 2, max = 32)
    private String fullName;

    @JsonProperty("password")
    @NotEmpty
    @NotBlank
    @Size(min = 8, max = 32)
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;

    @JsonProperty("captcha")
    @NotNull
    @NotBlank
    private String captcha;
}
