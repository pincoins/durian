package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.FavoriteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long>, FavoriteItemRepositoryQuery {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM FavoriteItem fi WHERE fi.user.id = :userId AND fi.product.id = :productId")
    int delete(@Param("userId") Long userId, @Param("productId") Long productId);
}
