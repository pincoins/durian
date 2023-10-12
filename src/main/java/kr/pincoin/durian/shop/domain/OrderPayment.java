package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.shop.domain.conveter.PaymentAccount;
import kr.pincoin.durian.shop.domain.conveter.PaymentAccountConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account")
    @NotNull
    @Convert(converter = PaymentAccountConverter.class)
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
