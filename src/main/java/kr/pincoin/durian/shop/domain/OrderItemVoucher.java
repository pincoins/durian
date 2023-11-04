package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shop_order_item_voucher")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class OrderItemVoucher {
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
                                                  String remarks,
                                                  OrderItem orderItem,
                                                  Voucher voucher) {
        return new OrderItemVoucherBuilder()
                .code(code)
                .remarks(remarks)
                .orderItem(orderItem)
                .voucher(voucher)
                .revoked(false);
    }
}
