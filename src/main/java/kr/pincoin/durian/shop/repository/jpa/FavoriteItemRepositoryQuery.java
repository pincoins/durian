package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.FavoriteItem;
import kr.pincoin.durian.shop.domain.Product;

import java.util.List;
import java.util.Optional;

public interface FavoriteItemRepositoryQuery {
    List<Product> findProducts(Long userId);

    Optional<FavoriteItem> findItem(Long userId, Long productId);
}
