package at.collew.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * dto class for the user patch
 * @author Patrick Stadt
 * @version 1
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangeInfoDto {
    public String firstname;
    public String lastname;
    public String phonenumber;
}
