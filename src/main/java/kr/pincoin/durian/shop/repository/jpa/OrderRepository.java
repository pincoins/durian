package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryQuery {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Order o SET o.sending = :#{#order.sending} WHERE o = :order")
    void updateSendingStatus(@Param("order") Order order);
}
