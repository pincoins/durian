package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, OrderItemRepositoryQuery {
}
