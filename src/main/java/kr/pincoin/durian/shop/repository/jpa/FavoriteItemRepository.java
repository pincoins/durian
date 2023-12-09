package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.FavoriteItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long>, FavoriteItemRepositoryQuery {
}
