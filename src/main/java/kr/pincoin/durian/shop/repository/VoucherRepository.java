package kr.pincoin.durian.shop.repository;

import kr.pincoin.durian.shop.domain.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long>, VoucherRepositoryQuery {
}
