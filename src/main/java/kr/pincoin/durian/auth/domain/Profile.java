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
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public Profile(Builder builder) {
        this.user = builder.user;
        this.phoneVerification = builder.phoneVerification;
        this.documentVerification = builder.documentVerification;
        this.allowOrder = builder.allowOrder;
        this.notPurchasedMonths = builder.notPurchasedMonths;
        this.totalOrderCount = builder.totalOrderCount;
        this.maxPrice = builder.maxPrice;
        this.totalListPrice = builder.totalListPrice;
        this.totalSellingPrice = builder.totalSellingPrice;
        this.averagePrice = builder.averagePrice;
        this.mileage = builder.mileage;
    }

    public static class Builder {
        private final User user;

        private final PhoneVerification phoneVerification;

        private final DocumentVerification documentVerification;

        private boolean allowOrder;

        private boolean notPurchasedMonths;

        private Integer totalOrderCount;

        private BigDecimal maxPrice;

        private BigDecimal totalListPrice;

        private BigDecimal totalSellingPrice;

        private BigDecimal averagePrice;

        private BigDecimal mileage;

        public Builder(User user,
                       PhoneVerification phoneVerification,
                       DocumentVerification documentVerification) {
            this.user = user;
            this.phoneVerification = phoneVerification;
            this.documentVerification = documentVerification;

            this.allowOrder = false;
            this.notPurchasedMonths = false;
            this.totalOrderCount = 0;
            this.maxPrice = BigDecimal.ZERO;
            this.totalListPrice = BigDecimal.ZERO;
            this.totalSellingPrice = BigDecimal.ZERO;
            this.averagePrice = BigDecimal.ZERO;
            this.mileage = BigDecimal.ZERO;
        }

        public Builder allowOrder(boolean allowOrder) {
            this.allowOrder = allowOrder;
            return this;
        }

        public Builder notPurchasedMonths(boolean notPurchasedMonths) {
            this.notPurchasedMonths = notPurchasedMonths;
            return this;
        }

        public Builder totalOrderCount(Integer totalOrderCount) {
            this.totalOrderCount = totalOrderCount;
            return this;
        }

        public Builder maxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public Builder totalListPrice(BigDecimal totalListPrice) {
            this.totalListPrice = totalListPrice;
            return this;
        }

        public Builder totalSellingPrice(BigDecimal totalSellingPrice) {
            this.totalSellingPrice = totalSellingPrice;
            return this;
        }

        public Builder averagePrice(BigDecimal averagePrice) {
            this.averagePrice = averagePrice;
            return this;
        }

        public Builder mileage(BigDecimal mileage) {
            this.mileage = mileage;
            return this;
        }

        public Profile build() {
            return new Profile(this);
        }
    }
}
