package at.collew.account.service.impl;

import at.collew.account.dto.AppointmentDto;
import at.collew.account.model.Appointment;
import at.collew.account.model.User_Location;
import at.collew.account.repository.AppointmentRepository;
import at.collew.account.repository.User_LocationRepository;
import at.collew.account.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Patrick Stadt
 * @version 2
 */

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private User_LocationServiceUtil locations;

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private User_LocationRepository locationRepository;

    @Override
    public void createAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = new Appointment();
        try {
            appointment.setUserLocationId(locations.locationIdToLocation(appointmentDto.getLocationid()));
            appointment.setDay(appointmentDto.getDay());
            appointment.setStartTime(appointmentDto.getStartTime());
            appointment.setEndTime(appointmentDto.getEndTime());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        repository.save(appointment);
    }

    @Override
    public void removeAppointment(Appointment appointment) {
        User_Location location = appointment.getUserLocationId();
        location.getAppointments().remove(appointment);
        locationRepository.save(location);
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        repository.save(appointment);
    }
}
