package kr.pincoin.durian.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.auth.domain.converter.VerificationStatus;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "auth_profile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Profile extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "email_verified_status")
    @Enumerated(value = EnumType.STRING)
    private VerificationStatus emailVerification;

    @Embedded
    private PhoneVerification phoneVerification;

    @Embedded
    private DocumentVerification documentVerification;

    @Column(name = "allow_order")
    @Builder.Default
    private boolean allowOrder = false;

    @Column(name = "total_order_count")
    @Builder.Default
    private Integer totalOrderCount = 0;

    @Column(name = "first_purchased")
    private LocalDateTime firstPurchased;

    @Column(name = "last_purchased")
    private LocalDateTime lastPurchased;

    @Column(name = "not_purchased_months")
    @Builder.Default
    private boolean notPurchasedMonths = false;

    @Column(name = "repurchased")
    private LocalDateTime repurchased;

    @Column(name = "max_price")
    @Builder.Default
    private BigDecimal maxPrice = BigDecimal.ZERO;

    @Column(name = "total_list_price")
    @Builder.Default
    private BigDecimal totalListPrice = BigDecimal.ZERO;

    @Column(name = "total_selling_price")
    @Builder.Default
    private BigDecimal totalSellingPrice = BigDecimal.ZERO;

    @Column(name = "average_price")
    @Builder.Default
    private BigDecimal averagePrice = BigDecimal.ZERO;

    @Column(name = "mileage")
    @Builder.Default
    private BigDecimal mileage = BigDecimal.ZERO;

    @Column(name = "memo")
    private String memo;

    @OneToOne(optional = false,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public static ProfileBuilder builder(VerificationStatus emailVerification,
                                         PhoneVerification phoneVerification,
                                         DocumentVerification documentVerification) {
        return new ProfileBuilder()
                .emailVerification(emailVerification)
                .phoneVerification(phoneVerification)
                .documentVerification(documentVerification);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return id != null && Objects.equals(id, profile.id) && Objects.equals(user, profile.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }

    public Profile belongsTo(User user) {
        this.user = user;
        return this;
    }

    public Profile addTransaction(BigDecimal totalListPrice, BigDecimal totalSellingPrice) {
        lastPurchased = LocalDateTime.now();

        if (firstPurchased == null) {
            firstPurchased = LocalDateTime.now();
        }

        if (notPurchasedMonths && repurchased != null) {
            repurchased = lastPurchased;
        }

        this.totalListPrice = this.totalListPrice.add(totalListPrice);
        this.totalSellingPrice = this.totalSellingPrice.add(totalSellingPrice);
        this.totalOrderCount += 1;

        if (maxPrice == null || maxPrice.compareTo(totalSellingPrice) < 0) {
            maxPrice = totalSellingPrice;
        }

        averagePrice = this.totalListPrice
                .divide(BigDecimal.valueOf(this.totalOrderCount + 1), 0, RoundingMode.DOWN);

        return this;
    }
}
