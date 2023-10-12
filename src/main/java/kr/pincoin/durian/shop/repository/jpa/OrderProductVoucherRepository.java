package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.OrderProductVoucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductVoucherRepository
        extends JpaRepository<OrderProductVoucher, Long>, OrderProductVoucherRepositoryQuery {
}
