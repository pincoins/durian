package kr.pincoin.durian.auth.repository.jpa;

import kr.pincoin.durian.auth.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProfileRepository extends JpaRepository<Profile, Long>, ProfileRepositoryQuery {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Profile p" +
            " SET p.lastPurchased = :#{#profile.lastPurchased}," +
            " p.firstPurchased = :#{#profile.firstPurchased}," +
            " p.notPurchasedMonths = :#{#profile.notPurchasedMonths}," +
            " p.maxPrice = :#{#profile.maxPrice}," +
            " p.averagePrice = :#{#profile.averagePrice}," +
            " p.totalOrderCount = :#{#profile.totalOrderCount}," +
            " p.totalListPrice = :#{#profile.totalListPrice}," +
            " p.totalSellingPrice = :#{#profile.totalSellingPrice}" +
            " WHERE p = :profile")
    void updateTransaction(@Param("profile") Profile profile);
}
