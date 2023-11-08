package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.common.domain.BaseDateTime;
import kr.pincoin.durian.shop.controller.dto.OrderCreateRequest;
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

    @Column(name = "sending")
    @Enumerated(value = EnumType.STRING)
    private SendingStatus sending;

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

    public static OrderBuilder builder(OrderCreateRequest request,
                                       Profile profile,
                                       HttpServletRequest httpServletRequest) {
        String ipAddress = Optional.ofNullable(httpServletRequest.getHeader("X-Forwarded-For"))
                .orElse(httpServletRequest.getRemoteAddr());

        String userAgent = Optional.ofNullable(httpServletRequest.getHeader("User-Agent"))
                .orElse("No user-agent set");

        String acceptLanguage = Optional.ofNullable(httpServletRequest.getHeader("Accept-Language"))
                .orElse("No language set");

        return new OrderBuilder()
                .paymentMethod(request.getPaymentMethod())
                .orderUuid(UUID.randomUUID().toString())
                .status(OrderStatus.ORDERED)
                .payment(PaymentStatus.UNPAID)
                .sending(SendingStatus.NOT_SENT)
                .visible(OrderVisibility.VISIBLE)
                .user(profile.getUser())
                .fullName(profile.getUser().getFullName())
                .userAgent(userAgent)
                .acceptLanguage(acceptLanguage)
                .ipAddress(ipAddress)
                .suspicious(false)
                .removed(false);
    }

    public Order add(OrderItem orderItem) {
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
            orderItem.belongsTo(this);
        }

        return this;
    }

    public Order add(OrderPayment orderPayment) {
        if (!payments.contains(orderPayment)) {
            payments.add(orderPayment);
        }

        if (orderPayment.getOrder() != this) {
            orderPayment.belongsTo(this);
        }

        return this;
    }

    public Order changePrice(BigDecimal totalListPrice, BigDecimal totalSellingPrice) {
        this.totalListPrice = totalListPrice;
        this.totalSellingPrice = totalSellingPrice;

        return this;
    }

    public Order changePaymentStatus(PaymentStatus payment) {
        this.payment = payment;
        return this;
    }

    public Order changeSendingStatus(SendingStatus sending) {
        this.sending = sending;
        return this;
    }

    public BigDecimal getTotalPaidAmount() {
        return payments.stream()
                .map(OrderPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isFullyPaid() {
        return getTotalPaidAmount().compareTo(totalSellingPrice) > 0;
    }
}
