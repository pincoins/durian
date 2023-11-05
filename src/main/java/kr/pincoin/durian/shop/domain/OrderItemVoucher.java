package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.*;

@Entity
@Table(name = "shop_order_item_voucher")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class OrderItemVoucher extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_voucher_id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "revoked")
    private boolean revoked;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY) // optional = true
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    public static OrderItemVoucherBuilder builder(String code,
                                                  String remarks) {
        return new OrderItemVoucherBuilder()
                .code(code)
                .remarks(remarks)
                .revoked(false);
    }

    public OrderItemVoucher makeOrderItem(OrderItem orderItem) {
        if (this.orderItem != null) {
            this.orderItem.getVouchers().remove(this);
        }

        this.orderItem = orderItem;

        if (!orderItem.getVouchers().contains(this)) {
            orderItem.getVouchers().add(this);
        }

        return this;
    }
}
