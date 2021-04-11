package org.cafe.management.domain;

import lombok.Data;
import org.cafe.management.enums.OrderStatusType;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Data
@Entity(name = "cafe_order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private TableEntity table;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<ProductInOrderEntity> productInOrders;

    @Enumerated(EnumType.STRING)
    private OrderStatusType status;

}
