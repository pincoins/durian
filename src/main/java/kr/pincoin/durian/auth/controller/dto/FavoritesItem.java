package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritesItem {
    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("slug")
    @NotNull
    @NotBlank
    private String slug;

    @JsonProperty("title")
    @NotNull
    @NotBlank
    private String title;
}
