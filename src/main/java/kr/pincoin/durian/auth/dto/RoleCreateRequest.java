package kr.pincoin.durian.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleCreateRequest {
    public static final String ROLE_CODE_PATTERN = "^ROLE_[A-Z0-9]+$";

    public static final String ROLE_NAME_PATTERN = "^[A-Z0-9]+$";

    @JsonProperty("code")
    @Pattern(regexp = ROLE_CODE_PATTERN)
    private String code;

    @JsonProperty("name")
    @Pattern(regexp = ROLE_NAME_PATTERN)
    private String name;

    public RoleCreateRequest(String code,
                             String name) {
        this.code = code;
        this.name = name;
    }
}
