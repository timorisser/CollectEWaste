package at.collew.account.util;

import lombok.*;

/**
 * This class is a data class for Responses of type user
 * @author Patrick Stadt
 * @version 2
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String phonenumber;
    private String companyName;
    private boolean enabled;
}
