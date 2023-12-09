package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.converter.UserStatus;
import kr.pincoin.durian.auth.repository.jpa.ProfileRepository;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.domain.FavoriteItem;
import kr.pincoin.durian.shop.domain.Product;
import kr.pincoin.durian.shop.domain.conveter.ProductStatus;
import kr.pincoin.durian.shop.repository.jpa.FavoriteItemRepository;
import kr.pincoin.durian.shop.repository.jpa.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FavoritesService {
    private final FavoriteItemRepository favoriteItemRepository;

    private final ProductRepository productRepository;

    private final ProfileRepository profileRepository;

    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public List<Product>
    listFavoriteItems(Long userId) {
        return favoriteItemRepository.findItems(userId);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public Optional<Product>
    createFavoriteItem(Long userId, Long productId) {
        Profile profile = profileRepository.findMember(userId, UserStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Invalid profile",
                                                    List.of("Normal profile does not exist.")));

        Product product = productRepository.findProduct(productId,
                                                        null,
                                                        null,
                                                        ProductStatus.ENABLED,
                                                        null,
                                                        false)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Invalid product",
                                                    List.of("Normal product does not exist.")));

        FavoriteItem favoriteItem = FavoriteItem.builder(profile.getUser(), product).build();

        favoriteItemRepository.save(favoriteItem);

        return Optional.of(product);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF') or hasRole('MEMBER') and @identity.isOwner(#userId)")
    public boolean removeFavoriteItem(Long userId, Long productId) {
        return favoriteItemRepository.delete(userId, productId) > 0;
    }
}
