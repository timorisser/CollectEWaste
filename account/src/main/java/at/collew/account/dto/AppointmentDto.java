package at.collew.account.dto;

import at.collew.account.model.Days;
import lombok.*;

import java.sql.Time;

/**
 * dto class for the appointment
 * @author Patrick Stadt
 * @version 1
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentDto {

    private int locationid;

    private Days day;

    private Time startTime;

    private Time endTime;

    @Override
    public String toString() {
        return "AppointmentDto{" +
                "locationid=" + locationid +
                ", day=" + day +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

}
