package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderItemVoucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemVoucherRepository
        extends JpaRepository<OrderItemVoucher, Long>, OrderItemVoucherRepositoryQuery {
}
