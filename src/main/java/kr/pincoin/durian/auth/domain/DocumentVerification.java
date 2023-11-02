package kr.pincoin.durian.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.pincoin.durian.auth.domain.converter.VerificationStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentVerification {
    @Column(name = "document_verified_status")
    @Enumerated(value = EnumType.STRING)
    private VerificationStatus documentVerifiedStatus;

    @Column(name = "photo_id")
    private String photoId;

    @Column(name = "card")
    private String card;

    public DocumentVerification(VerificationStatus phoneVerifiedStatus) {
        this.documentVerifiedStatus = phoneVerifiedStatus;
    }

    public DocumentVerification verify(String photoId, String card) {
        this.documentVerifiedStatus = VerificationStatus.VERIFIED;

        this.photoId = photoId;
        this.card = card;

        return this;
    }

    public DocumentVerification reject() {
        this.documentVerifiedStatus = VerificationStatus.UNVERIFIED;
        invalidate();
        return this;
    }

    public DocumentVerification revoke() {
        this.documentVerifiedStatus = VerificationStatus.REVOKED;
        return this;
    }

    private void invalidate() {
        this.photoId = null;
        this.card = null;
    }
}
