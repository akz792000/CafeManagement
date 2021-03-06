package org.cafe.management.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cafe.management.enums.RoleType;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@NoArgsConstructor
@Data
@Entity(name = "cafe_user")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<TableEntity> tables;

}
