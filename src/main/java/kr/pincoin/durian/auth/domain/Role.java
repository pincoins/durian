package kr.pincoin.durian.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    @NotNull
    @NotBlank
    private String code;

    @Column(name = "name")
    @NotNull
    @NotBlank
    private String name;

    public Role(String code,
                String name) {
        this.code = code;
        this.name = name;
    }
}
