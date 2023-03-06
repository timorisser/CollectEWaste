package at.collew.account.model;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Patrick Stadt
 * @version 1
 */

@Entity
@Table(name="appointment")
public class Appointment {

    /** The id of the appointment */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** The id of the user_location where the appointment points to */
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinColumn(name="userLocationId")
    private User_Location userLocationId;

    @Enumerated(EnumType.STRING)
    @Column(name="day")
    private Days day;

    /** The start_time from appointment */
    @Column(name="start_time")
    private Time startTime;

    /** The end_time from appointment */
    @Column(name="end_time")
    private Time endTime;

    /** Empty Constructor */
    public Appointment() {}


    /** Constructor */
    public Appointment(Integer id, User_Location userLocationId, Days day, Time startTime, Time endTime) {
        this.id = id;
        this.userLocationId = userLocationId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Days getDay() {
        return day;
    }

    public void setDay(Days day) {
        this.day = day;
    }

    /** Getter for id */
    public Integer getId() {
        return id;
    }

    /** Setter for id */
    public void setId(Integer id) {
        this.id = id;
    }

    /** Getter for location id */
    public User_Location getUserLocationId() {
        return userLocationId;
    }

    /** Setter for location id */
    public void setUserLocationId(User_Location userLocationId) {
        this.userLocationId = userLocationId;
    }

    /** Getter for start time */
    public Time getStartTime() {
        return startTime;
    }

    /** Setter for start time */
    public void setStartTime(Time start_time) {
        this.startTime = start_time;
    }

    /** Getter for end time */
    public Time getEndTime() {
        return endTime;
    }

    /** Setter for end time */
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", userLocationId=" + userLocationId.getId() +
                ", day=" + day +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
