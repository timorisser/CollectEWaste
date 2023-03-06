package at.collew.account.service.impl;

import at.collew.account.dto.UserDto;
import at.collew.account.jwtutils.TokenManager;
import at.collew.account.model.Users;
import at.collew.account.repository.UsersRepository;
import at.collew.account.service.UsersService;
import at.collew.account.util.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Patrick Stadt
 * @version 2
 */

@Service
public class UsersServiceImpl implements UsersService, UserDetailsService {
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenManager tokenManager;

    public Logger LOGGER = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Override
    @Transactional
    public void createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            LOGGER.error(".createUser: Error user already exist " + userDto.getEmail());
        }
        Users user = new Users();

        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhonenumber(userDto.getPhonenumber());
        user.setCompany(userDto.isCompany());
        user.setRegisterToken(tokenManager.generateEmailToken(userDto));
        user.setEnabled(false);

        if (!userDto.isCompany()) user.setCompanyName(null);
        if (userDto.isCompany() && userDto.getCompanyName()==null) user.setCompany(false);

        user.setCompanyName(userDto.getCompanyName());

        if (UserInfoValid(user.getCompanyName())) user.setCompany(true);

        LOGGER.info(".creatUser: User was created: " + user);
        userRepository.save(user);
    }

    @Override
    public Users findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUser(Users users) {
        if (users == null) {
            throw new UsernameNotFoundException("{UserServiceImpl.updateUser}: User is null");
        }
        userRepository.save(users);
    }


    public static UserDto mapToUserDto(Users user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public UserResponse mapToUserResponse(Users user) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public static boolean UserInfoValid(String information) {
        return information != null && !information.trim().isEmpty();
    }

    public static boolean isEmailValid(String information) {
        //Regular Expression by RFC 5322 for Email Validation
        return information.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    public Users getUserWithToken(String token) {
        return userRepository.findByEmail(tokenManager.getEmailFromToken(token));
    }

    public UserDto getUserDto(int user_id) {
        return mapToUserDto(userRepository.findById(user_id));
    }
}
