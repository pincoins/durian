package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.common.domain.BaseDateTime;
import kr.pincoin.durian.shop.domain.conveter.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "shop_order")
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

    @Column(name = "order_uuid")
    private String orderUuid;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "accept_language")
    private String acceptLanguage;

    @Column(name = "ip_address")
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
    @Builder.Default
    private BigDecimal totalListPrice = BigDecimal.ZERO;

    @Column(name = "total_selling_price")
    @Builder.Default
    private BigDecimal totalSellingPrice = BigDecimal.ZERO;

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

    @OneToMany(mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<OrderPayment> payments = new ArrayList<>();

    public static OrderBuilder builder(PaymentMethod paymentMethod,
                                       Profile profile,
                                       HttpServletRequest request) {
        String ipAddress = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .orElse(request.getRemoteAddr());

        String userAgent = Optional.ofNullable(request.getHeader("User-Agent"))
                .orElse("No user-agent set");

        String acceptLanguage = Optional.ofNullable(request.getHeader("Accept-Language"))
                .orElse("No language set");

        return new OrderBuilder()
                .paymentMethod(paymentMethod)
                .orderUuid(UUID.randomUUID().toString())
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
                .removed(false);
    }

    public Order addOrderItem(OrderItem orderItem) {
        if (!items.contains(orderItem)) {
            totalListPrice = totalListPrice
                    .add((orderItem.getPrice().getListPrice()
                            .multiply(BigDecimal.valueOf(orderItem.getQuantity()))));

            totalSellingPrice = totalSellingPrice
                    .add((orderItem.getPrice().getSellingPrice()
                            .multiply(BigDecimal.valueOf(orderItem.getQuantity()))));

            items.add(orderItem);
        }

        if (orderItem.getOrder() != this) {
            orderItem.makeOrder(this);
        }

        return this;
    }

    public Order addOrderPayment(OrderPayment orderPayment) {
        if (!payments.contains(orderPayment)) {
            payments.add(orderPayment);
        }

        if (orderPayment.getOrder() != this) {
            orderPayment.makeOrder(this);
        }

        return this;
    }

    public Order savePrice(BigDecimal totalListPrice, BigDecimal totalSellingPrice) {
        this.totalListPrice = totalListPrice;
        this.totalSellingPrice = totalSellingPrice;

        return this;
    }
}
