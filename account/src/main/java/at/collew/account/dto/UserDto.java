package at.collew.account.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * dto class for the user
 * @author Patrick Stadt
 * @version 2
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    @NotEmpty
    @NotBlank
    private String firstname;

    @NotEmpty
    @NotBlank
    private String lastname;

    @NotEmpty
    @Email(message ="Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotEmpty
    @NotBlank
    private String phonenumber;

    @NotEmpty
    @NotBlank
    private String password;

    private boolean isCompany;
    private String companyName;
}
