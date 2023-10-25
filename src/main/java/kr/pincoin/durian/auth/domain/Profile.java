package kr.pincoin.durian.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "address")
    private String address;

    @Embedded
    private PhoneVerification phoneVerification;

    @Embedded
    private DocumentVerification documentVerification;

    @Column(name = "allow_order")
    private boolean allowOrder;

    @Column(name = "total_order_count")
    private Integer totalOrderCount;

    @Column(name = "first_purchased")
    private LocalDateTime firstPurchased;

    @Column(name = "last_purchased")
    private LocalDateTime lastPurchased;

    @Column(name = "not_purchased_months")
    private boolean notPurchasedMonths;

    @Column(name = "repurchased")
    private LocalDateTime repurchased;

    @Column(name = "max_price")
    private BigDecimal maxPrice;

    @Column(name = "total_list_price")
    private BigDecimal totalListPrice;

    @Column(name = "total_selling_price")
    private BigDecimal totalSellingPrice;

    @Column(name = "average_price")
    private BigDecimal averagePrice;

    @Column(name = "mileage")
    private BigDecimal mileage;

    @Column(name = "memo")
    private String memo;

    @OneToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public Profile(User user,
                   PhoneVerification phoneVerification,
                   DocumentVerification documentVerification,
                   boolean allowOrder,
                   boolean notPurchasedMonths,
                   Integer totalOrderCount,
                   BigDecimal maxPrice,
                   BigDecimal totalListPrice,
                   BigDecimal totalSellingPrice,
                   BigDecimal averagePrice,
                   BigDecimal mileage) {
        this.user = user;
        this.phoneVerification = phoneVerification;
        this.documentVerification = documentVerification;
        this.allowOrder = allowOrder;
        this.notPurchasedMonths = notPurchasedMonths;
        this.totalOrderCount = totalOrderCount;
        this.maxPrice = maxPrice;
        this.totalListPrice = totalListPrice;
        this.totalSellingPrice = totalSellingPrice;
        this.averagePrice = averagePrice;
        this.mileage = mileage;
    }

    public Profile(User user,
                   PhoneVerification phoneVerification,
                   DocumentVerification documentVerification) {
        this(user,
             phoneVerification,
             documentVerification,
             false,
             false,
             0,
             BigDecimal.ZERO,
             BigDecimal.ZERO,
             BigDecimal.ZERO,
             BigDecimal.ZERO,
             BigDecimal.ZERO);
    }
}
