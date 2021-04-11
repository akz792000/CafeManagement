package org.cafe.management.domain;

import lombok.Data;
import org.cafe.management.enums.ProductInOrderStatusType;

import javax.persistence.*;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Data
@Entity(name = "cafe_product_in_order")
public class ProductInOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderEntity order;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private ProductInOrderStatusType status;

}
