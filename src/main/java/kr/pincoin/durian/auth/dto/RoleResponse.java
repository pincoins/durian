package kr.pincoin.durian.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.auth.domain.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    public RoleResponse(Long id,
                        String code,
                        String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.code = role.getCode();
        this.name = role.getName();
    }
}
