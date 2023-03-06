package at.collew.account.service.impl;


import at.collew.account.dto.AppointmentDto;
import at.collew.account.jwtutils.JwtFilter;
import at.collew.account.model.Appointment;
import at.collew.account.model.User_Location;
import at.collew.account.model.Users;
import at.collew.account.repository.AppointmentRepository;
import at.collew.account.repository.User_LocationRepository;
import at.collew.account.util.AppointmentResponse;
import at.collew.account.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Patrick Stadt
 * @version 1
 */

@Service
public class AppointmentServiceUtil {

    @Autowired
    private JwtFilter filter;

    @Autowired
    private UsersServiceImpl usersService;

    @Autowired
    private AppointmentServiceImpl appointmentService;

    @Autowired
    private User_LocationRepository locationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private User_LocationServiceUtil locationUtil;

    private final ResponseHandler responseHandler = new ResponseHandler();


    public ResponseEntity<?> saveNewAppointment(AppointmentDto appointmentDto, HttpServletRequest request) {
        if (!this.isLocationIDfromUser(appointmentDto, request))
            return responseHandler.generateResponse("The Location you entered is not yours", HttpStatus.BAD_REQUEST, null);
        if (appointmentDto.getEndTime().before(appointmentDto.getStartTime()))
            return responseHandler.generateResponse("End is before start", HttpStatus.BAD_REQUEST, null);

        appointmentService.createAppointment(appointmentDto);
        return responseHandler.generateResponse(null, HttpStatus.OK, this.returnListAsResponse(this.returnAppointmentList(appointmentDto)));
    }

    public ResponseEntity<?> getSpecificAppointment(int appointment_id) {
        Appointment appointment;
        try {
            appointment = appointmentRepository.findAppointmentById(appointment_id);
            if (appointment==null)
                return responseHandler.generateResponse("AppointmentID was not found", HttpStatus.BAD_REQUEST, null);
        } catch (NullPointerException e) {
            return responseHandler.generateResponse("The appointment does not exists", HttpStatus.BAD_REQUEST, null);
        }
        return responseHandler.generateResponse(null, HttpStatus.OK, this.mapToAppointmentResponse(appointment));
    }

    public ResponseEntity<?> getAppointmentsFromUserLocation(int userLocationId) {
        User_Location location;
        List<Appointment> appointments;
        try {
            location = locationRepository.findUser_LocationsById(userLocationId);
            appointments = new ArrayList<>(location.getAppointments());
        } catch (NullPointerException e) {
            return responseHandler.generateResponse("Location was not found", HttpStatus.BAD_REQUEST, null);
        }

        return responseHandler.generateResponse(null, HttpStatus.OK, this.returnListAsResponse(appointments));
    }

    public ResponseEntity<?> editAppointment(int appointment_id, AppointmentDto appointmentDto, HttpServletRequest request) {
        Appointment appointment;
        try {
            appointment = appointmentRepository.findAppointmentById(appointment_id);
            if (appointment==null)
                return responseHandler.generateResponse("AppointmentID was not found", HttpStatus.BAD_REQUEST, null);
        } catch (NullPointerException e) {
            return responseHandler.generateResponse("The appointment does not exists", HttpStatus.BAD_REQUEST, null);
        }
        if (appointment.getUserLocationId().getUserId().getId()!=filter.getIdFromToken(filter.getAccessToken(request)))
            return responseHandler.generateResponse("You are not the owner of this appointment", HttpStatus.UNAUTHORIZED, null);
        if (!this.isLocationIDfromUser(appointmentDto, request))
            return responseHandler.generateResponse("The Location you entered is not yours", HttpStatus.BAD_REQUEST, null);


        if (appointment.getUserLocationId().getId()!=appointmentDto.getLocationid()) appointment.setUserLocationId(locationUtil.locationIdToLocation(appointmentDto.getLocationid()));
        if (appointment.getDay()!=appointmentDto.getDay()) appointment.setDay(appointmentDto.getDay());
        if (appointmentDto.getStartTime()!=null && appointmentDto.getEndTime()!=null && appointmentDto.getStartTime().before(appointmentDto.getEndTime())) {
            appointment.setStartTime(appointmentDto.getStartTime());
            appointment.setEndTime(appointmentDto.getEndTime());
        }

        appointmentService.updateAppointment(appointment);
        return responseHandler.generateResponse(null, HttpStatus.OK, this.mapToAppointmentResponse(appointment));
    }

    public ResponseEntity<?> removeAppointment(int appointment_id, HttpServletRequest request) {
        Appointment appointment = appointmentRepository.findAppointmentById(appointment_id);
        if (appointment==null)
            return responseHandler.generateResponse("AppointmentID was not found", HttpStatus.BAD_REQUEST, null);

        List<User_Location> locations = usersService.getUserWithToken(filter.getAccessToken(request)).getLocations();

        Optional<User_Location> locationWithAppointment = locations.stream()
                .filter(location -> location.getAppointments().contains(appointment))
                .findAny();

        if (locationWithAppointment.isPresent()) {
            appointmentService.removeAppointment(appointment);
            return responseHandler.generateResponse(null, HttpStatus.OK, "Deleted");
        } else {
            return responseHandler.generateResponse("Error", HttpStatus.BAD_REQUEST, null);
        }
    }

    public boolean isLocationIDfromUser(AppointmentDto appointmentDto, HttpServletRequest request) {
        Users user = usersService.getUserWithToken(filter.getAccessToken(request));
        Set<Integer> locationIds = user.getLocations().stream().map(User_Location::getId).collect(Collectors.toSet());
        return locationIds.contains(appointmentDto.getLocationid());
    }

    public AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setId(appointment.getId());
        appointmentResponse.setLocationid(appointment.getUserLocationId().getId());
        appointmentResponse.setDay(appointment.getDay());
        appointmentResponse.setStartTime(appointment.getStartTime());
        appointmentResponse.setEndTime(appointment.getEndTime());
        return appointmentResponse;
    }

    public List<AppointmentResponse> returnListAsResponse(List<Appointment> appointments) {
        return appointments.stream().map(this::mapToAppointmentResponse).collect(Collectors.toList());
    }

    public List<Appointment> returnAppointmentList(AppointmentDto appointmentDto) {
        return locationRepository.findUser_LocationsById(appointmentDto.getLocationid()).getAppointments();
    }

}
