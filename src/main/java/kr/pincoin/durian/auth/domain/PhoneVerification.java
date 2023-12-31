package kr.pincoin.durian.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.pincoin.durian.auth.domain.converter.ProfileDomestic;
import kr.pincoin.durian.auth.domain.converter.ProfileGender;
import kr.pincoin.durian.auth.domain.converter.ProfileTelecom;
import kr.pincoin.durian.auth.domain.converter.VerificationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneVerification {
    @Column(name = "phone")
    private String phone;

    @Column(name = "phone_verified_status")
    @Enumerated(value = EnumType.STRING)
    private VerificationStatus phoneVerifiedStatus;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private ProfileGender gender;

    @Column(name = "domestic")
    @Enumerated(value = EnumType.STRING)
    private ProfileDomestic domestic;

    @Column(name = "telecom")
    @Enumerated(value = EnumType.STRING)
    private ProfileTelecom telecom;

    public PhoneVerification(VerificationStatus phoneVerifiedStatus) {
        this.phoneVerifiedStatus = phoneVerifiedStatus;
    }

    public PhoneVerification verify(String phone,
                                    LocalDate dateOfBirth,
                                    ProfileGender gender,
                                    ProfileDomestic domestic,
                                    ProfileTelecom telecom) {
        this.phoneVerifiedStatus = VerificationStatus.VERIFIED;

        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.domestic = domestic;
        this.telecom = telecom;

        return this;
    }

    public PhoneVerification reject() {
        this.phoneVerifiedStatus = VerificationStatus.UNVERIFIED;
        invalidate();
        return this;
    }

    public PhoneVerification revoke() {
        this.phoneVerifiedStatus = VerificationStatus.REVOKED;
        invalidate();
        return this;
    }

    private void invalidate() {
        this.phone = null;
        this.dateOfBirth = null;
        this.gender = null;
        this.domestic = null;
        this.telecom = null;
    }
}
