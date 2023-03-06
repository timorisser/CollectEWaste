package at.collew.account.service;

import at.collew.account.dto.UserDto;
import at.collew.account.model.Users;

/**
 * @author Patrick Stadt
 * @version 2
 */
public interface UsersService {
    /**
     * we create a new user in the database
     * @param userDto data object that inhibits the data from the user
     */
    void createUser(UserDto userDto);

    /**
     * finds user by email
     * @param email email of the user
     * @return the user that uses the specific email
     */
    Users findUserByEmail(String email);

    /**
     * update user method
     * @param users user
     */
    void updateUser(Users users);

}
