package at.collew.account.service;

import at.collew.account.dto.LocationDto;
import at.collew.account.model.User_Location;
import at.collew.account.model.Users;


/**
 * @author Patrick Stadt
 * @version 2
 */
public interface User_LocationService {

    /**
     * saves new location in database
     * @param locationDto dto
     * @param users user
     */
    void saveLocation(LocationDto locationDto, Users users);

    /**
     * remove location by id
     * @param location user location
     */
    void removeLocation(User_Location location);


    /**
     * updates user location via id
     * @param location new location that should be updated
     */
    void updateLocation(User_Location location);
}
