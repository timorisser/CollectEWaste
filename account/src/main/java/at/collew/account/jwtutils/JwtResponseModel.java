package at.collew.account.jwtutils;


import java.io.Serial;
import java.io.Serializable;

/**
 * @author Patrick Stadt
 * @version 1
 */
public record JwtResponseModel(int id, String email, boolean is_company, String token) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
