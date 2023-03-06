package at.collew.account.jwtutils;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Patrick Stadt
 * @version 1
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequestModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 2636936156391265891L;

    private String email;
    private String password;

}
