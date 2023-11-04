package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.shop.domain.conveter.PaymentAccount;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shop_order_payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderPayment {
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
}
