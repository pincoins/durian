package kr.pincoin.durian.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDateTime {
    @CreatedDate
    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "modified", nullable = false)
    private LocalDateTime modified;
}
