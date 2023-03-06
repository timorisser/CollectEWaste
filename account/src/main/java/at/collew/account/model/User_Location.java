package at.collew.account.model;

import at.collew.account.dto.LocationDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * model class for userLocation
 * @author Patrick Stadt
 * @version 1
 */

@Entity
@Table(name="user_location")
public class User_Location implements Comparable<LocationDto> {

    /** The id of the user_location */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** The id of the user where the user_location points to */
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private Users userId;

    /** The location from the user */
    @Column(name="location")
    private String location;

    /** The street from the location */
    @Column(name="street")
    private String street;

    /** The street number from the location */
    @Column(name="street_number")
    private int streetNumber;

    /** The stair from the user */
    @Column(name="stair")
    private int stair;

    /** The door from the user */
    @Column(name="door")
    private int door;

    /** THe state from the user */
    @Enumerated(EnumType.STRING)
    @Column(name="state")
    private States state;

    /** The postcode from the location */
    @Column(name="postcode")
    private int postcode;

    /** The appointments that are mapped to the location */
    @OneToMany(mappedBy = "userLocationId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    /** Empty Constructor */
    public User_Location() {}

    /** Constructor */
    public User_Location(Integer id, Users userId, String location, String street, int streetNumber, int stair, int door, States state, int postcode) {
        this.id = id;
        this.userId = userId;
        this.location = location;
        this.street = street;
        this.streetNumber = streetNumber;
        this.stair = stair;
        this.door = door;
        this.state = state;
        this.postcode = postcode;
    }

    /** Getter for id  */
    public Integer getId() {
        return id;
    }

    /** Setter for id */
    public void setId(Integer id) {
        this.id = id;
    }

    /** Getter for the User id */
    public Users getUserId() {
        return userId;
    }

    /** Setter for the user id */
    public void setUserId(Users userId) {
        this.userId = userId;
    }

    /** Getter for the street */
    public String getStreet() {
        return street;
    }

    /** Setter for the street */
    public void setStreet(String street) {
        this.street = street;
    }

    /** Getter for the street number */
    public int getStreetNumber() {
        return streetNumber;
    }

    /** Setter for the street number*/
    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    /** Getter for the postcode */
    public int getPostcode() {
        return postcode;
    }

    /** Getter for the location */
    public String getLocation() {
        return location;
    }

    /** Setter for the location */
    public void setLocation(String location) {
        this.location = location;
    }

    /** Setter for the postcode */
    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public int getStair() {
        return stair;
    }

    public void setStair(int stair) {
        this.stair = stair;
    }

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User_Location{" +
                "id=" + id +
                ", userId=" + userId.getId() +
                ", location=" + location +
                ", street=" + street +
                ", streetNumber=" + streetNumber +
                ", stair=" + stair +
                ", door=" + door +
                ", state=" + state +
                ", postcode=" + postcode +
                '}';
    }

    @Override
    public int compareTo(LocationDto o) {
        return (location.compareTo(o.getLocation()) == 0 &&
            streetNumber == o.getStreetNumber() &&
            stair == o.getStair() &&
            door == o.getDoor() &&
            postcode == o.getPostcode() &&
            street.compareTo(o.getStreet()) == 0 &&
            state.compareTo(o.getState()) == 0) ? 0 : 1;
    }
}
