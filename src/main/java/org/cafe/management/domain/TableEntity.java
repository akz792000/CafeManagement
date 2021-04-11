package org.cafe.management.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Data
@Entity(name = "cafe_table")
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "table", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<OrderEntity> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

}
