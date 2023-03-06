package at.collew.account.repository;

import at.collew.account.model.User_Location;
import at.collew.account.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * repository for user_location
 * @author Patrick Stadt
 * @version 2
 */
@Repository
public interface User_LocationRepository extends JpaRepository<User_Location, Integer> {

    /**
     * find userlocation by user id
     * @param userId the user
     * @return the user_location that references to the users id
     */
    User_Location findUser_LocationByUserId(Users userId);

    /**
     * checks if the locations already is mapped to the user
     * @param userId id from the user
     * @return boolean if the locations exists
     */
    boolean existsUser_LocationByUserId(Users userId);

    /**
     * find all locations from user
     * @param userId id from user
     * @return list of all locations that are mapped to the specific user
     */
    List<User_Location> findUser_LocationsByUserId(Users userId);

    /**
     * returns location that correlates to the id
     * @param id id of the location in the database
     * @return the user_location mapped to the id
     */
    User_Location findUser_LocationsById(int id);
}