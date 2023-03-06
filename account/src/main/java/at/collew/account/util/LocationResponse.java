package at.collew.account.util;

import at.collew.account.model.States;
import lombok.*;

/**
 * This class is a data class for Responses of type Location
 * @author Patrick Stadt
 * @version 1
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocationResponse {
    private int id;
    private int userid;
    private String location;
    private String street;
    private int streetNumber;
    private int stair;
    private int door;
    private States state;
    private int postcode;

}
