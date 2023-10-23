package kr.pincoin.durian.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentVerification {
    @Column(name = "document_verified")
    private boolean documentVerified;

    @Column(name = "photo_id")
    private String photoId;

    @Column(name = "card")
    private String card;

    public DocumentVerification(boolean documentVerified) {
        this.documentVerified = documentVerified;
    }
}
