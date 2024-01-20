package ru.flanker.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author 1ommy
 * @version 14.01.2024
 */
@Getter
@Setter
@ToString
@Embeddable
public class DateInformation {

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime deletedAt;
}
