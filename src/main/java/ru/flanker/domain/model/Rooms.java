package ru.flanker.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

/**
 * @author 1ommy
 * @version 14.01.2024
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "rooms")
public class Rooms extends BaseClass {
    @Column
    private Integer number;

    @Column
    private String description;

    @ManyToOne
    private User bookedBy;
}
