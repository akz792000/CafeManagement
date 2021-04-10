package org.cafe.management.web.dto;

import lombok.Data;
import org.cafe.management.constraint.FieldMatch;

import javax.validation.constraints.NotEmpty;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Data
public class ProductDto {

    @NotEmpty
    private String name;

}
