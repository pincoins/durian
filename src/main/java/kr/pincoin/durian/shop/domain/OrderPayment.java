package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.common.domain.BaseDateTime;
import kr.pincoin.durian.shop.domain.conveter.PaymentAccount;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shop_order_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class OrderPayment extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_payment_id")
    private Long id;

    @Column(name = "account")
    @Enumerated(value = EnumType.STRING)
    private PaymentAccount account;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "received")
    private LocalDateTime received;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public static OrderPaymentBuilder builder(PaymentAccount account,
                                              BigDecimal amount,
                                              BigDecimal balance) {
        return new OrderPaymentBuilder()
                .account(account)
                .amount(amount)
                .balance(balance)
                .received(LocalDateTime.now());
    }

    public OrderPayment makeOrder(Order order) {
        if (this.order != null) {
            this.order.getPayments().remove(this);
        }

        this.order = order;

        if (!order.getPayments().contains(this)) {
            order.getPayments().add(this);
        }

        return this;
    }
}
