package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.common.domain.BaseDateTime;
import kr.pincoin.durian.shop.controller.dto.VoucherUpdateRequest;
import kr.pincoin.durian.shop.domain.conveter.VoucherStatus;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "shop_voucher")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Voucher extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long id;

    @Column(name = "is_removed")
    private boolean removed;

    @Column(name = "code")
    private String code;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private VoucherStatus status;

    public static VoucherBuilder builder(String code, String remarks, Product product) {
        return new VoucherBuilder()
                .code(code)
                .remarks(remarks)
                .product(product)
                .status(VoucherStatus.PURCHASED)
                .removed(false);
    }

    public Voucher changeProduct(Product product) {
        if (this.product != null) {
            this.product.getVouchers().remove(this);
        }

        this.product = product;

        if (!product.getVouchers().contains(this)) {
            product.getVouchers().add(this);
        }

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voucher voucher = (Voucher) o;
        return id != null && Objects.equals(id, voucher.id) && Objects.equals(code, voucher.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

    public Voucher purchased() {
        this.status = VoucherStatus.PURCHASED;
        return this;
    }

    public Voucher sold() {
        this.status = VoucherStatus.SOLD;
        return this;
    }

    public Voucher revoked() {
        this.status = VoucherStatus.REVOKED;
        return this;
    }

    public Voucher remove() {
        this.removed = true;
        return this;
    }

    public Voucher restore() {
        this.removed = false;
        return this;
    }

    public Voucher update(VoucherUpdateRequest request) {
        this.code = request.getCode();
        this.remarks = request.getRemarks();
        return this;
    }
}
