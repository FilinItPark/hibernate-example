package ru.flanker.domain.model;

import jakarta.persistence.*;
import lombok.*;
import ru.flanker.domain.model.enums.UserRoles;

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
@Table(name = "users")
public class User extends BaseClass {
    @Column
    private String username;

    @Column
    private String fullName;

    @Enumerated(value = EnumType.STRING)
    private UserRoles role;
}
