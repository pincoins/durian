package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Favorites {
    @JsonProperty("items")
    @NotNull
    private List<FavoritesItem> items = new ArrayList<>();

    public static FavoritesBuilder builder() {
        return new FavoritesBuilder().items(List.of());
    }
}
