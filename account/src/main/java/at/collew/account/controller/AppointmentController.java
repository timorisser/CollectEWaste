package at.collew.account.controller;

import at.collew.account.dto.AppointmentDto;
import at.collew.account.service.impl.AppointmentServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Patrick Stadt
 * @version 1
 */

@RestController
@RequestMapping(value ="/api/v1/account/appointment", produces = "application/json")
@CrossOrigin
public class AppointmentController {

    @Autowired
    private AppointmentServiceUtil appointments;

    @PostMapping
    public ResponseEntity<?> newAppointment(@RequestBody AppointmentDto appointmentDto, HttpServletRequest request) {
        return appointments.saveNewAppointment(appointmentDto, request);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getAppointment(@PathVariable int appointmentId) {
        return appointments.getSpecificAppointment(appointmentId);
    }

    @PatchMapping("/{appointmentId}")
    public ResponseEntity<?> patchAppointment(@PathVariable int appointmentId, @RequestBody AppointmentDto appointmentDto, HttpServletRequest request) {
        return appointments.editAppointment(appointmentId, appointmentDto, request);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@PathVariable int appointmentId, HttpServletRequest request) {
        return appointments.removeAppointment(appointmentId, request);
    }

    @GetMapping("/loc/{userLocationId}")
    public ResponseEntity<?> getAppointmentFromLocation(@PathVariable int userLocationId) {
        return appointments.getAppointmentsFromUserLocation(userLocationId);
    }
}
