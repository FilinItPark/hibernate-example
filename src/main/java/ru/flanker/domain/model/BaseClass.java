package ru.flanker.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 1ommy
 * @version 14.01.2024
 */
@MappedSuperclass
@Getter
@Setter
@ToString
public abstract class BaseClass {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    protected Integer id;

    @Embedded
    protected DateInformation dateInformation;
}
