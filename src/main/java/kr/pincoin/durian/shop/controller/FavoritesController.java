package kr.pincoin.durian.shop.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.FavoritesRequest;
import kr.pincoin.durian.shop.controller.dto.ProductResponse;
import kr.pincoin.durian.shop.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class FavoritesController {
    private final FavoritesService favoritesService;

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>>
    favoritesList(@RequestParam(name = "userId") Long userId) {
        return ResponseEntity
                .ok()
                .body(favoritesService.listFavoriteItems(userId)
                              .stream()
                              .map(ProductResponse::new)
                              .toList());
    }

    @PostMapping("")
    public ResponseEntity<ProductResponse>
    favoritesAdd(@Valid @RequestBody FavoritesRequest request) {
        return favoritesService.addFavoriteItem(request.getUserId(), request.getProductId())
                .map(product -> ResponseEntity.ok().body(new ProductResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT,
                                                    "Favorite item addition failure",
                                                    List.of("Failed to create a new favorite item.")));
    }

    @DeleteMapping("")
    public ResponseEntity<ProductResponse>
    favoritesRemove(@Valid @RequestBody FavoritesRequest request) {
        if (favoritesService.removeFavoriteItem(request.getUserId(), request.getProductId())) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
