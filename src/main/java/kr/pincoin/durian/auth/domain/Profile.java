package kr.pincoin.durian.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.auth.domain.converter.PhoneVerifiedStatus;
import kr.pincoin.durian.auth.domain.converter.ProfileDomestic;
import kr.pincoin.durian.auth.domain.converter.ProfileGender;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_verified")
    private boolean phoneVerified;

    @Column(name = "phone_verified_status")
    @NotNull
    private PhoneVerifiedStatus phoneVerifiedStatus;

    @Column(name = "document_verified")
    private boolean documentVerified;

    @Column(name = "allow_order")
    private boolean allowOrder;

    @Column(name = "photo_id")
    private String photoId;

    @Column(name = "card")
    private String card;

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

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private ProfileGender gender;

    @Column(name = "domestic")
    private ProfileDomestic domestic;

    @Column(name = "telecom")
    private String telecom;

    @OneToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public Profile(User user,
                   boolean phoneVerified,
                   PhoneVerifiedStatus phoneVerifiedStatus,
                   boolean documentVerified,
                   boolean allowOrder,
                   boolean notPurchasedMonths,
                   Integer totalOrderCount,
                   BigDecimal maxPrice,
                   BigDecimal totalListPrice,
                   BigDecimal totalSellingPrice,
                   BigDecimal averagePrice,
                   BigDecimal mileage) {
        this.user = user;

        this.phoneVerified = phoneVerified;
        this.phoneVerifiedStatus = phoneVerifiedStatus;
        this.documentVerified = documentVerified;
        this.allowOrder = allowOrder;
        this.notPurchasedMonths = notPurchasedMonths;
        this.totalOrderCount = totalOrderCount;
        this.maxPrice = maxPrice;
        this.totalListPrice = totalListPrice;
        this.totalSellingPrice = totalSellingPrice;
        this.averagePrice = averagePrice;
        this.mileage = mileage;
    }

    public Profile(User user) {
        this(user,
             false,
             PhoneVerifiedStatus.UNVERIFIED,
             false,
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
