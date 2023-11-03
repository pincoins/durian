package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface VoucherRepository extends JpaRepository<Voucher, Long>, VoucherRepositoryQuery {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE OrderItemVoucher oiv SET oiv.voucher = null WHERE oiv.voucher = :voucher")
    void disconnect(@Param("voucher") Voucher voucher);
}
