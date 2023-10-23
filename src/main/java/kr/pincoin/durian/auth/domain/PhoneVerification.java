package kr.pincoin.durian.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.pincoin.durian.auth.domain.converter.PhoneVerifiedStatus;
import kr.pincoin.durian.auth.domain.converter.ProfileDomestic;
import kr.pincoin.durian.auth.domain.converter.ProfileGender;
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

    @Column(name = "phone_verified")
    private boolean phoneVerified;

    @Column(name = "phone_verified_status")
    @Enumerated(value = EnumType.STRING)
    private PhoneVerifiedStatus phoneVerifiedStatus;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private ProfileGender gender;

    @Column(name = "domestic")
    @Enumerated(value = EnumType.STRING)
    private ProfileDomestic domestic;

    @Column(name = "telecom")
    private String telecom;

    public PhoneVerification(boolean phoneVerified,
                             PhoneVerifiedStatus phoneVerifiedStatus) {
        this.phoneVerified = phoneVerified;
        this.phoneVerifiedStatus = phoneVerifiedStatus;
    }
}
