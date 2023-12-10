package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorites {
    @JsonProperty("items")
    @NotNull
    private List<FavoritesItem> items = new ArrayList<>();
}
