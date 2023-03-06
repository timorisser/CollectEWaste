package at.collew.account.service.impl;

import at.collew.account.dto.LocationDto;
import at.collew.account.model.User_Location;
import at.collew.account.model.Users;
import at.collew.account.repository.User_LocationRepository;
import at.collew.account.repository.UsersRepository;
import at.collew.account.service.User_LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Patrick Stadt
 * @version 3
 */

@Service
public class User_LocationServiceImpl implements User_LocationService {

    @Autowired
    private User_LocationRepository user_locationRepository;

    @Autowired
    private UsersRepository usersRepository;


    @Override
    public void saveLocation(LocationDto locationDto, Users users) {
        User_Location location = new User_Location();
        try {
            location.setUserId(users);
            location.setLocation(locationDto.getLocation());
            location.setStreet(locationDto.getStreet());
            location.setStreetNumber(locationDto.getStreetNumber());
            location.setStair(locationDto.getStair());
            location.setDoor(locationDto.getDoor());
            location.setState(locationDto.getState());
            location.setPostcode(locationDto.getPostcode());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        user_locationRepository.save(location);
    }
    @Override
    public void removeLocation(User_Location location) {
        Users user = location.getUserId();
        user.getLocations().remove(location);
        usersRepository.save(user);
        //due to jpa annotations and child parent behavior we delete the location from the parent object
        //because if we don't we delete all the locations from the user and also the user itself
    }


    @Override
    public void updateLocation(User_Location location) {
        user_locationRepository.save(location);
    }

}