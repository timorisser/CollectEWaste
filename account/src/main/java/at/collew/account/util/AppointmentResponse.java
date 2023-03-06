package at.collew.account.util;

import at.collew.account.model.Days;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

/**
 * This class is a data class for Responses of type appointment
 * @author Patrick Stadt
 * @version 1
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentResponse {
    private int id;
    private int locationid;
    private Days day;
    private Time startTime;
    private Time endTime;
}
