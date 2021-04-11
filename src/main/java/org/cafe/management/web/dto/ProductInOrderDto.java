package org.cafe.management.web.dto;

import lombok.Data;
import org.cafe.management.enums.ProductInOrderStatusType;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Data
public class ProductInOrderDto {

    private Long id;

    private ProductDto product;

    private OrderDto order;

    private Long amount;

    private ProductInOrderStatusType status;

}
