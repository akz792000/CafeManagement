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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "cafe_table_order",
            joinColumns = @JoinColumn(
                    name = "table_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "order_id", referencedColumnName = "id"))
    private Collection<OrderEntity> orders;


    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

}
