package at.collew.account.controller;

import at.collew.account.dto.ChangeInfoDto;
import at.collew.account.dto.PasswordDto;
import at.collew.account.dto.UserDto;
import at.collew.account.jwtutils.*;
import at.collew.account.service.impl.LoginService;
import at.collew.account.service.impl.RegistrationService;
import at.collew.account.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * RestController for Login and Register
 * @author Patrick Stadt
 * @version 1.6
 */
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value ="/api/v1/account", produces = "application/json")
public class UserController {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private RegistrationService registerService;

    @Autowired
    private LoginService loginService;


    @PostMapping("/register")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserDto userDto) {
        return registerService.register(userDto);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        return registerService.confirmToken(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody UserDto userDto) {
        return loginService.login(userDto);
    }

    @PatchMapping
    public ResponseEntity<?> changeInformation(@RequestBody ChangeInfoDto userDto, HttpServletRequest request) {
        return loginService.updateUser(userDto, request);
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDto passwordDto, HttpServletRequest request) {
        return loginService.changePassword(request, passwordDto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(HttpServletRequest request) {
        return loginService.softDeleteUser(request);
    }

    @GetMapping("/forgotPasswordEmail")
    public ResponseEntity<?> forgotPasswordEmail(@RequestParam("email") String email) {
        return registerService.forgotPasswordEmail(email);
    }

    @PatchMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam("token") String token, @RequestBody PasswordDto passwordDto) {
        return registerService.forgotPassword(passwordDto, token);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        return loginService.getUserBySpecificId(id);
    }

    @GetMapping("/sendChangeEmail")
    public ResponseEntity<?> changeEmailEmail(HttpServletRequest request) {
        return registerService.changeEmailEmail(jwtFilter.getAccessToken(request));
    }

    @PatchMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@RequestParam("token") String token,@RequestBody UserDto email , HttpServletRequest request) {
        return registerService.changeEmail(email.getEmail(), token, request);
    }

    @GetMapping
    public ResponseEntity<?> getUserInformation(HttpServletRequest request) {
        return loginService.getUserBySpecificId(jwtFilter.getIdFromToken(jwtFilter.getAccessToken(request)));
    }

    @GetMapping("/verifyToken")
    public ResponseEntity<?> verifyToken(@RequestParam("token") String token) {
        if (!tokenManager.validateJwtToken(token)) {
            return new ResponseHandler().generateResponse("Token is not valid", HttpStatus.UNAUTHORIZED, null);
        }

        return new ResponseHandler().generateResponse("", HttpStatus.OK, jwtFilter.getUserDetails(token));
    }


}
