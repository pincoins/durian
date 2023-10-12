package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long>, OrderPaymentRepositoryQuery {
}
