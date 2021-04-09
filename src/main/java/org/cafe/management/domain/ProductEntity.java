package org.cafe.management.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
@Entity(name = "cafe_product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<ProductInOrderEntity> productInOrders;

}
