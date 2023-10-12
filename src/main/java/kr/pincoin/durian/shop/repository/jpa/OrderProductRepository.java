package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>, OrderProductRepositoryQuery {
}
