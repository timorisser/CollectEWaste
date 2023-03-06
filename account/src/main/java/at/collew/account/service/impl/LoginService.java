package at.collew.account.service.impl;

import at.collew.account.dto.ChangeInfoDto;
import at.collew.account.dto.PasswordDto;
import at.collew.account.dto.UserDto;
import at.collew.account.jwtutils.JwtFilter;
import at.collew.account.jwtutils.JwtRequestModel;
import at.collew.account.jwtutils.JwtResponseModel;
import at.collew.account.jwtutils.TokenManager;
import at.collew.account.model.Users;
import at.collew.account.repository.UsersRepository;
import at.collew.account.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

/** Service for all login api functions
 * @author Patrick Stadt
 * @version 2
 */

@Service
public class LoginService {

    @Autowired
    private UsersServiceImpl userService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private PasswordEncoder encoder;

    private final ResponseHandler responseHandler = new ResponseHandler();


    /**
     * loginmethod for the user with the user request
     * @param userDto dto
     * @return responses
     */
    public ResponseEntity<?> login(UserDto userDto) {
        if (!UsersServiceImpl.isEmailValid(userDto.getEmail()))
            return responseHandler.generateResponse("Email is not valid", HttpStatus.BAD_REQUEST, null);
        try {
            if (!usersRepository.findByEmail(userDto.getEmail()).getEnabled())
                return responseHandler.generateResponse("User is not enabled", HttpStatus.UNAUTHORIZED, null);
        } catch (NullPointerException e) {
            return responseHandler.generateResponse("User does not exist, or is not activated", HttpStatus.NOT_FOUND, null);
        }

        try {
            JwtRequestModel requestModel = new JwtRequestModel(userDto.getEmail(), userDto.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestModel.getEmail(), requestModel.getPassword()));
        } catch (DisabledException e) {
            return responseHandler.generateResponse("User is disabled", HttpStatus.BAD_REQUEST, null);
        } catch (BadCredentialsException e) {
            if (!encoder.matches(usersRepository.findByEmail(userDto.getEmail()).getPassword(), userDto.getPassword()))
                return responseHandler.generateResponse("Password is wrong", HttpStatus.BAD_REQUEST, null);
            return responseHandler.generateResponse("Request was bad", HttpStatus.BAD_REQUEST, null);
        }

        final Users user = (Users) userService.loadUserByUsername(userDto.getEmail());
        final String jwtToken = tokenManager.generateJwtToken(user);

        return responseHandler.generateResponse(null, HttpStatus.OK, new JwtResponseModel(user.getId(), user.getEmail(), user.isCompany(), jwtToken));
    }

    /**
     * method to change the user password with the user request
     * @param request request for token
     * @param passwordDto dto
     * @return responses
     */
    public ResponseEntity<?> changePassword(HttpServletRequest request, PasswordDto passwordDto) {
        Users user;
        try {
            user = usersRepository.findByEmail(tokenManager.getEmailFromToken(jwtFilter.getAccessToken(request)));
        } catch (NullPointerException e) {
            return responseHandler.generateResponse("Nullpointer", HttpStatus.BAD_REQUEST, null);
        }
        if (!encoder.matches(passwordDto.getOldpassword(), user.getPassword()))
            return responseHandler.generateResponse("Old password is incorrect", HttpStatus.BAD_REQUEST, null);
        if (!Objects.equals(passwordDto.getNewpassword(), passwordDto.getMatchingpassword()))
            return responseHandler.generateResponse("Passwords not matching", HttpStatus.BAD_REQUEST, null);
        if (encoder.matches(passwordDto.getNewpassword(),user.getPassword()))
            return responseHandler.generateResponse("New password matches old password", HttpStatus.BAD_REQUEST, null);

        user.setPassword(encoder.encode(passwordDto.getNewpassword()));
        userService.updateUser(user);

        return responseHandler.generateResponse("Password successfuly changed", HttpStatus.OK, userService.mapToUserResponse(user));
    }

    /**
     * method to update the user with the user request
     * @param userDto dto
     * @param request request for token
     * @return responses
     */
    public ResponseEntity<?> updateUser(ChangeInfoDto userDto, HttpServletRequest request) {
        String token = jwtFilter.getAccessToken(request);
        if (tokenManager.isTokenExpired(token)) {
            return responseHandler.generateResponse("Token is expired", HttpStatus.UNAUTHORIZED, null);
        }
        Users helpUser = (Users) jwtFilter.getUserDetails(token);
        int userid = helpUser.getId();


        Optional<Users> toSaveUser = usersRepository.findById(helpUser.getId());

        if (toSaveUser.isEmpty()) return responseHandler.generateResponse("User is empty", HttpStatus.BAD_REQUEST, null);

        if (toSaveUser.get().getId() != userid) {
            return responseHandler.generateResponse("User-ID is False", HttpStatus.BAD_REQUEST, null);
        }
        if (UsersServiceImpl.UserInfoValid(userDto.getFirstname())) {
            toSaveUser.get().setFirstname(userDto.getFirstname());
        }
        if (UsersServiceImpl.UserInfoValid(userDto.getLastname())) {
            toSaveUser.get().setLastname(userDto.getLastname());
        }
        if (UsersServiceImpl.UserInfoValid(userDto.getPhonenumber())) {
            toSaveUser.get().setPhonenumber(userDto.getPhonenumber());
        }
        userService.updateUser(toSaveUser.get());

        return responseHandler.generateResponse(null, HttpStatus.OK, userService.mapToUserResponse(toSaveUser.get()));
    }

    /**
     * method to "delete" user with the user requests
     * @param request request for token
     * @return responses
     */
    public ResponseEntity<?> softDeleteUser(HttpServletRequest request) {
        Users user;
        try {
            user = usersRepository.findById(jwtFilter.getIdFromToken(jwtFilter.getAccessToken(request)));
        } catch (NullPointerException e) {
            return responseHandler.generateResponse("Id was not found", HttpStatus.BAD_REQUEST, null);
        }

        if (!user.getEnabled()) {
            return responseHandler.generateResponse("User is already disabled", HttpStatus.BAD_REQUEST, null); //responseObj is null because if user is already disabled and the user somehow is able to access this method there is something wrong
        }
        user.setEnabled(false);
        userService.updateUser(user);
        return responseHandler.generateResponse(null, HttpStatus.OK, userService.mapToUserResponse(user));
    }

    /**
     * method to get user information from the id with the user request
     * @param id userid
     * @return responses
     */
    public ResponseEntity<?> getUserBySpecificId(int id) {
        Users placeholder;
        try {
            placeholder = usersRepository.findById(id);
        } catch (NullPointerException e) {
            return responseHandler.generateResponse("Id was null or not found", HttpStatus.BAD_REQUEST, null);
        } catch (IllegalArgumentException e) {
            return responseHandler.generateResponse("Argument was illegal", HttpStatus.BAD_REQUEST, null);
        }
        try {
            return responseHandler.generateResponse(null, HttpStatus.OK, userService.mapToUserResponse(placeholder));
        } catch (NullPointerException e) {
            return responseHandler.generateResponse("Id was not found", HttpStatus.BAD_REQUEST, null);
        }
    }

    //TODO make a way a user can restore its account
}
