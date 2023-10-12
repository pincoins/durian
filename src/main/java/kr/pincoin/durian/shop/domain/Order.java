package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.common.domain.BaseDateTime;
import kr.pincoin.durian.shop.domain.conveter.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_removed")
    private boolean removed;

    @Column(name = "order_id", columnDefinition = "char")
    private String orderId;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "accept_language")
    private String acceptLanguage;

    @Column(name = "ip_address", columnDefinition = "char")
    private String ipAddress;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "payment_method")
    @NotNull
    @Convert(converter = PaymentMethodConverter.class)
    private PaymentMethod paymentMethod;

    @Column(name = "status")
    @NotNull
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;

    @Column(name = "visible")
    @NotNull
    @Convert(converter = OrderVisibilityConverter.class)
    private OrderVisibility visible;

    @Column(name = "total_list_price")
    private BigDecimal totalListPrice;

    @Column(name = "total_selling_price")
    private BigDecimal totalSellingPrice;

    @Column(name = "message")
    private String message;

    @Column(name = "suspicious")
    private boolean suspicious;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Order order;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
