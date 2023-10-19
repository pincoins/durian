package kr.pincoin.durian.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.converter.PhoneVerifiedStatus;
import kr.pincoin.durian.auth.domain.converter.ProfileDomestic;
import kr.pincoin.durian.auth.domain.converter.ProfileGender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileResponse extends UserResponse {
    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phoneVerified")
    private boolean phoneVerified;

    @JsonProperty("phoneVerifiedStatus")
    private PhoneVerifiedStatus phoneVerifiedStatus;

    @JsonProperty("documentVerified")
    private boolean documentVerified;

    @JsonProperty("allowOrder")
    private boolean allowOrder;

    @JsonProperty("photoId")
    private String photoId;

    @JsonProperty("card")
    private String card;

    @JsonProperty("totalOrderCount")
    private Integer totalOrderCount;

    @JsonProperty("firstPurchased")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime firstPurchased;

    @JsonProperty("lastPurchased")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastPurchased;

    @JsonProperty("notPurchasedMonths")
    private boolean notPurchasedMonths;

    @JsonProperty("repurchased")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime repurchased;

    @JsonProperty("maxPrice")
    private BigDecimal maxPrice;

    @JsonProperty("totalListPrice")
    private BigDecimal totalListPrice;

    @JsonProperty("totalSellingPrice")
    private BigDecimal totalSellingPrice;

    @JsonProperty("averagePrice")
    private BigDecimal averagePrice;

    @JsonProperty("mileage")
    private BigDecimal mileage;

    @JsonProperty("memo")
    private String memo;

    @JsonProperty("dateOfBirth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @JsonProperty("gender")
    private ProfileGender gender;

    @JsonProperty("domestic")
    private ProfileDomestic domestic;

    @JsonProperty("telecom")
    private String telecom;

    public UserProfileResponse(UserProfileResult result) {
        super(result.getUser());

        Profile profile = result.getProfile();

        this.phone = profile.getPhone();

        this.address = profile.getAddress();
        this.phoneVerified = profile.isPhoneVerified();
        this.phoneVerifiedStatus = profile.getPhoneVerifiedStatus();
        this.documentVerified = profile.isDocumentVerified();
        this.allowOrder = profile.isAllowOrder();
        this.photoId = profile.getPhotoId();
        this.card = profile.getCard();
        this.totalOrderCount = profile.getTotalOrderCount();
        this.firstPurchased = profile.getFirstPurchased();
        this.lastPurchased = profile.getLastPurchased();
        this.notPurchasedMonths = profile.isNotPurchasedMonths();
        this.repurchased = profile.getRepurchased();
        this.maxPrice = profile.getMaxPrice();
        this.totalListPrice = profile.getTotalListPrice();
        this.totalSellingPrice = profile.getTotalSellingPrice();
        this.averagePrice = profile.getAveragePrice();
        this.mileage = profile.getMileage();
        this.memo = profile.getMemo();
        this.dateOfBirth = profile.getDateOfBirth();
        this.gender = profile.getGender();
        this.domestic = profile.getDomestic();
        this.telecom = profile.getTelecom();
    }
}
