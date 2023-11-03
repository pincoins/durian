package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Product;
import kr.pincoin.durian.shop.domain.Voucher;
import kr.pincoin.durian.shop.domain.conveter.VoucherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long>, VoucherRepositoryQuery {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE OrderItemVoucher oiv SET oiv.voucher = null WHERE oiv.voucher = :voucher")
    void disconnect(@Param("voucher") Voucher voucher);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE OrderItemVoucher oiv SET oiv.voucher = null WHERE oiv.voucher.id in :ids")
    void disconnect(@Param("ids") List<Long> ids);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Voucher v SET v.status = :status WHERE v.id in :ids")
    int changeStatus(@Param("ids") List<Long> ids, @Param("status") VoucherStatus status);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Voucher v SET v.product = :product WHERE v.id in :ids")
    int changeProduct(@Param("ids") List<Long> ids, @Param("product") Product product);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Voucher v SET v.removed = :removed WHERE v.id in :ids")
    int toggleRemove(@Param("ids") List<Long> ids, @Param("removed") Boolean removed);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM Voucher v WHERE v.id in :ids")
    int delete(@Param("ids") List<Long> ids);
}
