package at.collew.account.service;

import at.collew.account.dto.AppointmentDto;
import at.collew.account.model.Appointment;

/**
 * @author Patrick Stadt
 * @version 1
 */
public interface AppointmentService {

    void createAppointment(AppointmentDto appointmentDto);

    void removeAppointment(Appointment appointment);

    void updateAppointment(Appointment appointment);
}
