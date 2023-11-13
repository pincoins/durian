package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCreateRequest {
    // letter, digit, period(.), underscore(_), hyphen(-)
    // neither start with nor end with period, underscore, hyphen
    // not consecutive period, underscore, hyphen like emoticons
    // Length 3~32 chars
    // Django: https://github.com/django/django/blob/main/django/contrib/auth/validators.py
    // regex = r"^[\w.@+-]+\Z" (letters, digits, @ . + - _)
    public static final String USERNAME_PATTERN = "^(?=.{3,32}$)(?![._-])(?!.*[._-]{2})[a-zA-Z0-9._-]+(?<![_.])$";

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
    @Pattern(regexp = USERNAME_PATTERN)
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

    @JsonProperty("email")
    @NotEmpty
    @NotBlank
    @Email
    private String email;
}
