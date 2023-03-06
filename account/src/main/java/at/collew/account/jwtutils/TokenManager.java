package at.collew.account.jwtutils;

import at.collew.account.dto.UserDto;
import at.collew.account.model.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Patrick Stadt
 * @version 1.1
 */

@Component
public class TokenManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 7008375124389347049L;

    public static final long TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 30; //30 days

    public Logger LOGGER = LoggerFactory.getLogger(TokenManager.class);

    @Value("${secret}")
    private String jwtSecret;

    public String generateJwtToken(Users user) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.format("%s,%s,%s", user.getId(), user.getEmail(), user.isCompany()))
                .setIssuer("CollEW Account Microservice")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateEmailToken(UserDto userDto) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDto.getEmail())
                .setIssuer("CollEW Account Microservice Mail")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.error("TokenManager.validateJwtToken: JWT expired", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("TokenManager.validateJwtToken: Token is null, empty or only whitespace", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("TokenManager.validateJwtToken: JWT is invalid", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("TokenManager.validateJwtToken: JWT is not supported", e.getMessage());
        }
        return false;
    }


    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getExpirationDateFromToken(String token) {
        return parseClaims(token).getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject().split(",")[1]; //subject looks like this {id, email} that's why we need to split the subject
    }

}
