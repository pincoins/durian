package kr.pincoin.durian.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.auth.domain.Role;
import kr.pincoin.durian.auth.domain.User;
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

    @JsonProperty("role")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String role;

    @JsonProperty("isActive")
    private boolean active;

    @JsonProperty("created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    public UserResponse(Long id,
                        String username,
                        String name,
                        Role role,
                        boolean active,
                        LocalDateTime created) {
        this.id = id;
        this.username = username;
        this.name = name;

        this.active = active;
        this.created = created;

        if (role != null) {
            this.role = role.getCode();
        }
    }

    public UserResponse(User user) {
        this(user.getId(),
             user.getUsername(),
             user.getName(),
             user.getRole(),
             user.isActive(),
             user.getCreated());
    }
}
