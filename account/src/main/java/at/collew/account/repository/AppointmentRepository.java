package at.collew.account.repository;

import at.collew.account.model.Appointment;
import at.collew.account.model.User_Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * repository for appointments
 * @author Patrick Stadt
 * @version 2
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    /**
     * method that gets the appointment according to its id
     * @param id id from the appointment
     * @return returns the specific appointment
     */
    Appointment findAppointmentById(int id);

    /**
     * list of appointments that are mapped to the specific location
     * @param userLocationId location
     * @return list of appointments that are mapped to the specific location
     */
    List<Appointment> findAppointmentsByUserLocationId(User_Location userLocationId);

}