package kr.pincoin.durian.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.auth.domain.DocumentVerification;
import kr.pincoin.durian.auth.domain.PhoneVerification;
import kr.pincoin.durian.auth.domain.Profile;
import kr.pincoin.durian.auth.domain.converter.VerificationStatus;
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
public class ProfileResponse extends UserResponse {
    @JsonProperty("address")
    private String address;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("dateOfBirth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @JsonProperty("gender")
    private ProfileGender gender;

    @JsonProperty("domestic")
    private ProfileDomestic domestic;

    @JsonProperty("telecom")
    private String telecom;

    @JsonProperty("phoneVerifiedStatus")
    private VerificationStatus phoneVerifiedStatus;

    @JsonProperty("photoId")
    private String photoId;

    @JsonProperty("card")
    private String card;

    @JsonProperty("documentVerifiedStatus")
    private VerificationStatus documentVerifiedStatus;

    @JsonProperty("allowOrder")
    private boolean allowOrder;

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

    public ProfileResponse(Profile profile) {
        super(profile.getUser());

        PhoneVerification phoneVerification = profile.getPhoneVerification();
        DocumentVerification documentVerification = profile.getDocumentVerification();

        this.address = profile.getAddress();

        this.phone = phoneVerification.getPhone();
        this.dateOfBirth = phoneVerification.getDateOfBirth();
        this.gender = phoneVerification.getGender();
        this.domestic = phoneVerification.getDomestic();
        this.telecom = phoneVerification.getTelecom();
        this.phoneVerifiedStatus = phoneVerification.getPhoneVerifiedStatus();

        this.photoId = documentVerification.getPhotoId();
        this.card = documentVerification.getCard();
        this.documentVerifiedStatus = documentVerification.getDocumentVerifiedStatus();

        this.allowOrder = profile.isAllowOrder();
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
    }
}
