package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.common.domain.BaseDateTime;
import kr.pincoin.durian.shop.domain.conveter.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Order extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "is_removed")
    private boolean removed;

    @Column(name = "order_uuid", columnDefinition = "char(32)")
    private String orderUuid;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "accept_language")
    private String acceptLanguage;

    @Column(name = "ip_address", columnDefinition = "char(39)")
    private String ipAddress;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "payment_method")
    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(name = "payment")
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus payment;

    @Column(name = "delivery")
    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus delivery;

    @Column(name = "visible")
    @Enumerated(value = EnumType.STRING)
    private OrderVisibility visible;

    @Column(name = "total_list_price")
    private BigDecimal totalListPrice;

    @Column(name = "total_selling_price")
    private BigDecimal totalSellingPrice;

    @Column(name = "message")
    private String message;

    @Column(name = "suspicious")
    private boolean suspicious;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Order order;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static OrderBuilder builder(PaymentMethod paymentMethod,
                                       String userAgent,
                                       String acceptLanguage,
                                       String ipAddress,
                                       Profile profile) {
        // transactionId
        // parent
        // memo

        return new OrderBuilder()
                .paymentMethod(paymentMethod)
                .orderUuid(UUID.randomUUID().toString().replace("-", ""))
                .status(OrderStatus.ORDERED)
                .payment(PaymentStatus.UNPAID)
                .delivery(DeliveryStatus.NOT_SENT)
                .visible(OrderVisibility.VISIBLE)
                .suspicious(false)
                .user(profile.getUser())
                .fullName(profile.getUser().getFullName())
                .userAgent(userAgent)
                .acceptLanguage(acceptLanguage)
                .ipAddress(ipAddress)
                .totalListPrice(profile.getTotalListPrice())
                .totalSellingPrice(profile.getTotalSellingPrice())
                .removed(false);
    }
}
