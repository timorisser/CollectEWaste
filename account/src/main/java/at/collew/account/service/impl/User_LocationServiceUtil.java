package at.collew.account.service.impl;

import at.collew.account.dto.LocationDto;
import at.collew.account.jwtutils.JwtFilter;
import at.collew.account.model.User_Location;
import at.collew.account.model.Users;
import at.collew.account.repository.User_LocationRepository;
import at.collew.account.util.LocationResponse;
import at.collew.account.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Patrick Stadt
 * @version 2
 */

@Service
public class User_LocationServiceUtil {

    @Autowired
    private User_LocationRepository user_locationRepository;

    @Autowired
    private User_LocationServiceImpl user_locationService;

    @Autowired
    private JwtFilter filter;

    @Autowired
    private UsersServiceImpl usersService;

    private final ResponseHandler responseHandler = new ResponseHandler();

    public ResponseEntity<?> saveUserLocation(LocationDto locationDto, HttpServletRequest request) {
        if (locationDto==null) return responseHandler.generateResponse("Location is null", HttpStatus.BAD_REQUEST, null);
        Users helpUser = usersService.getUserWithToken(filter.getAccessToken(request));

        if (this.getUserLocationWithDto(locationDto, helpUser)!=null) return responseHandler.generateResponse("Location already exists for your account", HttpStatus.BAD_REQUEST, null);

        user_locationService.saveLocation(locationDto, helpUser);
        return responseHandler.generateResponse(null, HttpStatus.OK, this.mapToLocationResponse(this.getUserLocationWithDto(locationDto, helpUser)));
    }

    public ResponseEntity<?> updateUserLocation(int location_id, LocationDto locationDto, HttpServletRequest request) {
        Users helpUser = usersService.getUserWithToken(filter.getAccessToken(request));

        User_Location newLocation = user_locationRepository.findUser_LocationsById(location_id);

        if (newLocation.getUserId()!=helpUser) {
            return responseHandler.generateResponse("Location does not match User", HttpStatus.BAD_REQUEST, null);
        }

        if (locationDto.getLocation() != null && !Objects.equals(locationDto.getLocation(), "")) newLocation.setLocation(locationDto.getLocation());
        if (locationDto.getStreet() != null && !Objects.equals(locationDto.getStreet(), "")) newLocation.setStreet( locationDto.getStreet());
        if (!(locationDto.getStreetNumber() == 0)) newLocation.setStreetNumber( locationDto.getStreetNumber());
        if (!(locationDto.getPostcode() == 0)) newLocation.setPostcode( locationDto.getPostcode());
        if (!(locationDto.getStair() == 0)) newLocation.setStair( locationDto.getStair());
        if (!(locationDto.getDoor() == 0)) newLocation.setDoor( locationDto.getDoor());
        if (locationDto.getState() != null) newLocation.setState( locationDto.getState());

        user_locationService.updateLocation(newLocation);
        return responseHandler.generateResponse(null, HttpStatus.OK, this.mapToLocationResponse(newLocation));
    }

    public ResponseEntity<?> getUserLocations(HttpServletRequest request) {
        Users helpUser = usersService.getUserWithToken(filter.getAccessToken(request));
        List<User_Location> user_locations = new ArrayList<>(helpUser.getLocations());

        return responseHandler.generateResponse(null, HttpStatus.OK, this.returnListAsResponse(user_locations));
    }

    public ResponseEntity<?> getUserLocation(int location_id) {
        User_Location help;
        try {
            help = user_locationRepository.findUser_LocationsById(location_id);
            if (help==null) return responseHandler.generateResponse("This location does not exists", HttpStatus.NOT_FOUND,null);
        } catch (Exception e) {
            return responseHandler.generateResponse("There was an error", HttpStatus.BAD_REQUEST, null);
        }
        return responseHandler.generateResponse(null, HttpStatus.OK, this.mapToLocationResponse(help));
    }


    public ResponseEntity<?> removeUserLocation(int location_id, HttpServletRequest request) {
        Users helpUser = usersService.getUserWithToken(filter.getAccessToken(request));
        List<User_Location> locations = new ArrayList<>(helpUser.getLocations());

        User_Location location = locations.stream()
                .filter(l -> l.getId() == location_id)
                .findAny()
                .orElse(null);

        if (location != null) {
            user_locationService.removeLocation(location);
            return responseHandler.generateResponse(null, HttpStatus.OK, "Location was successfully deleted");
        } else {
            return responseHandler.generateResponse("Location_id does not match any locations from you", HttpStatus.BAD_REQUEST, null);
        }
    }

    public LocationResponse mapToLocationResponse(User_Location location) {
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(location.getId());
        locationResponse.setUserid(location.getUserId().getId());
        locationResponse.setLocation(location.getLocation());
        locationResponse.setStreet(location.getStreet());
        locationResponse.setStreetNumber(location.getStreetNumber());
        locationResponse.setStair(location.getStair());
        locationResponse.setDoor(location.getDoor());
        locationResponse.setState(location.getState());
        locationResponse.setPostcode(location.getPostcode());
        return locationResponse;
    }

    public List<LocationResponse> returnListAsResponse(List<User_Location> user_locations) {
        return user_locations.stream()
                .map(this::mapToLocationResponse)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public User_Location getUserLocationWithDto(LocationDto locationDto, Users user) {
        List<User_Location> list = user_locationRepository.findUser_LocationsByUserId(user);
        return list.stream()
                .filter(g -> g.compareTo(locationDto)==0)
                .findAny()
                .orElse(null);
    }

    public User_Location locationIdToLocation(int id) {
        return user_locationRepository.findUser_LocationsById(id);
    }
}
