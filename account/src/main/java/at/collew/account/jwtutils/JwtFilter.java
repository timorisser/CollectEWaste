package at.collew.account.jwtutils;

import at.collew.account.model.Users;
import at.collew.account.service.impl.UsersServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Patrick Stadt
 * @version 1
 */

@Component
public class JwtFilter extends OncePerRequestFilter {

    public Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("JwtFilter.doFilterInternal: Request: " + request.toString());

        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(request);

        try {
            tokenManager.getEmailFromToken(token);
        } catch (ExpiredJwtException e) {
            LOGGER.error(".doFilterInternal: Expired: " + e.getMessage());
        }

        if (!tokenManager.validateJwtToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
        LOGGER.info(".doFilterInternal: Accepted");

    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return !ObjectUtils.isEmpty(header) && header.startsWith("Bearer");
    }

    public String getAccessToken(HttpServletRequest request) {
        return request.getHeader("Authorization").split(" ")[1].trim();
    }

    public void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usersService, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public UserDetails getUserDetails(String token) {
        Users userDetails = new Users();
        String[] jwtSubject = tokenManager.getSubject(token).split(",");

        userDetails.setId(Integer.parseInt(jwtSubject[0]));
        userDetails.setEmail(jwtSubject[1]);
        userDetails.setCompany(Boolean.parseBoolean(jwtSubject[2]));

        return userDetails;
    }

    public int getIdFromToken(String token) {
        String[] jwtSubject = tokenManager.getSubject(token).split(",");
        try {
            return Integer.parseInt(jwtSubject[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
