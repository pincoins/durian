package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Product;

import java.util.List;
import java.util.Optional;

public interface FavoriteItemRepositoryQuery {
    List<Product> findItems(Long userId);

    Optional<Product> findItem(Long userId, Long productId);
}
