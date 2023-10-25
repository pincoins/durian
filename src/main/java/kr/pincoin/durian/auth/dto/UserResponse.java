package kr.pincoin.durian.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.domain.converter.Role;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponse {
    @JsonProperty("userId")
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

    @JsonProperty("role")
    private Role role;

    public UserResponse(Long id,
                        String username,
                        String name,
                        Role role,
                        UserStatus status,
                        LocalDateTime created) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
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
