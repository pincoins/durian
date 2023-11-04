package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shop_order_item_voucher")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_voucher_id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "revoked")
    private boolean revoked;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY) // optional = true
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
}
