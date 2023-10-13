package kr.pincoin.durian.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChangePasswordRequest {
    @JsonProperty("oldPassword")
    @NotNull
    @NotBlank
    @Size(min = 8, max = 32)
    private String oldPassword;

    @JsonProperty("newPassword")
    @NotNull
    @NotBlank
    @Size(min = 8, max = 32)
    private String newPassword;

    public UserChangePasswordRequest(@NotNull String oldPassword,
                                     @NotNull String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
