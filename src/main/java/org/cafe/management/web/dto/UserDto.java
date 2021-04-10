package org.cafe.management.web.dto;

import lombok.Data;
import org.cafe.management.constraint.FieldMatch;
import org.cafe.management.enums.RoleType;

import javax.validation.constraints.NotEmpty;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Data
@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
})
public class UserDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;

    private RoleType roleType;

}
