package org.cafe.management.domain;

import lombok.Data;
import org.cafe.management.enums.ProductInOrderStatusType;

import javax.persistence.*;

@Data
@Entity(name = "cafe_product_in_order")
public class ProductInOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ProductInOrderStatusType status;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderEntity order;

}
