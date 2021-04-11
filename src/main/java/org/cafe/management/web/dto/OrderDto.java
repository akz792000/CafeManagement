package org.cafe.management.web.dto;

import lombok.Data;
import org.cafe.management.enums.OrderStatusType;

import javax.validation.constraints.NotEmpty;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Data
public class OrderDto {

    private Long id;

    @NotEmpty
    private String name;

    private TableDto table = new TableDto();

    private OrderStatusType status;

}
