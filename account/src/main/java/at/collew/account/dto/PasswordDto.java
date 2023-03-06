package at.collew.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto class for the password
 * @author Patrick Stadt
 * @version 2
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordDto {
    private String newpassword;
    private String matchingpassword;
    private String oldpassword;
}
