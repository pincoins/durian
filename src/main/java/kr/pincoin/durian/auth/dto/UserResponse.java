package kr.pincoin.durian.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.auth.domain.Role;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @JsonProperty("status")
    private UserStatus status;

    @JsonProperty("created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonProperty("roleId")
    private Long roleId;

    @JsonProperty("roleCode")
    private String roleCode;

    @JsonProperty("roleName")
    private String roleName;

    public UserResponse(Long id,
                        String username,
                        String name,
                        Role role,
                        UserStatus status,
                        LocalDateTime created) {
        this.id = id;
        this.username = username;
        this.name = name;

        if (role != null) {
            this.roleId = role.getId();
            this.roleCode = role.getCode();
            this.roleName = role.getName();
        }

        this.status = status;
        this.created = created;
    }

    public UserResponse(User user) {
        this(user.getId(),
             user.getUsername(),
             user.getName(),
             user.getRole(),
             user.getStatus(),
             user.getCreated());
    }
}
