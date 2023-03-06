package at.collew.account.controller;

import at.collew.account.dto.LocationDto;
import at.collew.account.service.impl.User_LocationServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Patrick Stadt
 * @version 3
 */

@RestController
@RequestMapping(value ="/api/v1/account/location", produces = "application/json")
@CrossOrigin
public class LocationController {

    @Autowired
    private User_LocationServiceUtil userLocation;

    @GetMapping
    public ResponseEntity<?> getMyLocations(HttpServletRequest request) {
        return userLocation.getUserLocations(request);
    }

    @GetMapping("/{location_id}")
    public ResponseEntity<?> getSpecificLocation(@PathVariable int location_id) {
        return userLocation.getUserLocation(location_id);
    }

    @PostMapping
    public ResponseEntity<?> newLocation(@Valid @RequestBody LocationDto locationDto, HttpServletRequest request) {
        return userLocation.saveUserLocation(locationDto, request);
    }

    @DeleteMapping("/{location_id}")
    public ResponseEntity<?> deleteLocation(@PathVariable int location_id, HttpServletRequest request) {
        return userLocation.removeUserLocation(location_id, request);
    }

    @PatchMapping("/{location_id}")
    public ResponseEntity<?> patchLocation(@PathVariable int location_id, @RequestBody LocationDto locationDto, HttpServletRequest request) {
        return userLocation.updateUserLocation(location_id, locationDto, request);
    }
}
