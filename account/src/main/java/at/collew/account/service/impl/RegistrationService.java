package at.collew.account.service.impl;

import at.collew.account.dto.PasswordDto;
import at.collew.account.dto.UserDto;
import at.collew.account.jwtutils.JwtFilter;
import at.collew.account.jwtutils.TokenManager;
import at.collew.account.mail.EMail;
import at.collew.account.mail.EmailSender;
import at.collew.account.model.Users;
import at.collew.account.repository.UsersRepository;
import at.collew.account.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Patrick Stadt
 * @version 1.5
 */

@Service
public class RegistrationService {
    private final String FORGOT_EMAIL = "changeEmail";
    private final String FORGOT_PASSWORD = "forgotPassword";

    @Autowired
    private UsersServiceImpl userService;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${project.url}")
    private String projectUrl;

    private final ResponseHandler responseHandler = new ResponseHandler();

    public ResponseEntity<?> register(UserDto userDto) {
        //Regular Expression by RFC 5322 for Email Validation
        if (!UsersServiceImpl.isEmailValid(userDto.getEmail()))
            return responseHandler.generateResponse("Email is not a valid email", HttpStatus.BAD_REQUEST, null);
        
        if (userRepository.existsByEmail(userDto.getEmail()))
            return responseHandler.generateResponse("User already exists", HttpStatus.BAD_REQUEST, null);

        userService.createUser(userDto);

        Map<String, Object> model = new HashMap<>();
        model.put("firstname", userDto.getFirstname());
        model.put("link", projectUrl+"confirm?token=" + userRepository.findByEmail(userDto.getEmail()).getRegisterToken());
        emailSender.sendEmail(new EMail(userDto.getEmail(), "Email bestätigen", "testcontent", model), "register-email.ftlh");

        return responseHandler.generateResponse(null, HttpStatus.OK, userService.mapToUserResponse(userService.findUserByEmail(userDto.getEmail())));
    }

    @Transactional
    public ResponseEntity<?> confirmToken(String token) {
        String emailUser = tokenManager.getSubject(token);
        Users toSave = userRepository.findByEmail(emailUser);
        if (!Objects.equals(token, toSave.getRegisterToken()))
            return responseHandler.generateResponse("Token does not match RegisterToken mapped to User", HttpStatus.BAD_REQUEST, null);
        if (tokenManager.validateJwtToken(token)) {
            toSave.setRegisterToken(null);
            toSave.setEnabled(true);
            userService.updateUser(toSave);
            return responseHandler.generateResponse(null, HttpStatus.OK, userService.mapToUserResponse(toSave));
        }
        return responseHandler.generateResponse("Bad Request",HttpStatus.BAD_REQUEST, null);
    }

    public ResponseEntity<?> forgotPasswordEmail(String email) {
        Users user = userRepository.findByEmail(email);
        if (user==null) return responseHandler.generateResponse("User is null", HttpStatus.NOT_FOUND, null);

        UserDto u = UsersServiceImpl.mapToUserDto(userService.findUserByEmail(email));


        Map<String, Object> model = new HashMap<>();
        model.put("firstname", u.getFirstname());
        model.put("link", this.generateLink(FORGOT_PASSWORD, u));
        emailSender.sendEmail(new EMail(u.getEmail(), "Passwort ändern", "testcontent", model), "forgot-password-email.ftlh");


        return responseHandler.generateResponse(null, HttpStatus.OK, userService.mapToUserResponse(user));
    }

    public ResponseEntity<?> forgotPassword(PasswordDto passwordDto, String token) {
        Users user = userService.getUserWithToken(token);

        if (!Objects.equals(passwordDto.getNewpassword(), passwordDto.getMatchingpassword()))
            return responseHandler.generateResponse("Password is not matching!", HttpStatus.BAD_REQUEST, null);

        user.setPassword(encoder.encode(passwordDto.getNewpassword()));
        userService.updateUser(user);
        return responseHandler.generateResponse(null, HttpStatus.OK, userService.mapToUserResponse(user));
    }

    public ResponseEntity<?> changeEmailEmail(String token) {
        Users user = userService.getUserWithToken(token);
        UserDto u = userService.getUserDto(user.getId());

        Map<String, Object> model = new HashMap<>();
        model.put("firstname", u.getFirstname());
        model.put("link", this.generateLink(FORGOT_EMAIL, u));
        emailSender.sendEmail(new EMail(u.getEmail(), "Email ändern", "testcontent", model), "change-email-email.ftlh");

        return responseHandler.generateResponse(null, HttpStatus.OK, userService.mapToUserResponse(user));
    }

    public ResponseEntity<?> changeEmail(String email, String token, HttpServletRequest request) {
        String userToken = jwtFilter.getAccessToken(request);
        // checks if the email in the logintoken is expired due to having change the email, a new token must be created for that user
        if (userRepository.findByEmail(tokenManager.getEmailFromToken(userToken))==null)
            return responseHandler.generateResponse("The email in the token is not mapped by any user, try refreshing your token", HttpStatus.BAD_REQUEST, null);

        if (!UsersServiceImpl.isEmailValid(email))
            return responseHandler.generateResponse("Email is not an email", HttpStatus.BAD_REQUEST, null);


        Users user = userService.getUserWithToken(userToken);
        if (!Objects.equals(tokenManager.getEmailFromToken(userToken), tokenManager.getSubject(token)))
            return responseHandler.generateResponse("Token do not match", HttpStatus.BAD_REQUEST, null);
        if (userService.findUserByEmail(email)!=null)
            return responseHandler.generateResponse("This email is already in use: " + email, HttpStatus.BAD_REQUEST, null);

        user.setEmail(email);
        userService.updateUser(user);
        return responseHandler.generateResponse(null, HttpStatus.OK, userService.mapToUserResponse(user));
    }

    private String generateLink(String path, UserDto u) {
        return projectUrl + path + "?token=" + tokenManager.generateEmailToken(u);
    }
}