package org.cafe.management.web.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Data
public class OrderDto {

    @NotEmpty
    private String name;

    private TableDto table = new TableDto();

}
