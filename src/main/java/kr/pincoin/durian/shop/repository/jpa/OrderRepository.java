package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryQuery {
}
