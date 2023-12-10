package kr.pincoin.durian.shop.controller;

import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.shop.controller.dto.ProductResponse;
import kr.pincoin.durian.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class FavoritesController {
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>>
    favoritesList(@AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;

        if (user == null) {
            return ResponseEntity.ok().body(List.of());
        }

        return ResponseEntity
                .ok()
                .body(productService.listFavoriteItems(user.getId())
                              .stream()
                              .map(ProductResponse::new)
                              .toList());
    }
}
