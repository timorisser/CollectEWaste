package at.collew.account.dto;

import at.collew.account.model.States;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * dto class for the location
 * @author Patrick Stadt
 * @version 1
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocationDto {
    @NotBlank
    @NotEmpty
    private String location; //TODO maybe rename location to city
    @NotBlank
    @NotEmpty
    private String street;
    private int streetNumber;
    private int stair;
    private int door;
    private States state;
    @NotNull(message= "postcode may not be empty")
    private int postcode;
}
