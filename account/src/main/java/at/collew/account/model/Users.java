package at.collew.account.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Patrick Stadt
 * @version 1.2
 */

@Entity
@Table(name="users")
public class Users implements UserDetails {

    /** The id of the user */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id; // in the long run it would be wise to change "Integer" to "Long" because it can store more values but for now it should be fine

    /** The firstname of the user */
    @Column(name="firstname")
    private String firstname;

    /** The lastname of the user */
    @Column(name="lastname")
    private String lastname;

    /** The email of the user */
    @Column(name="email", unique = true)
    private String email;

    /** The phonenumber of the user */
    @Column(name="phonenumber")
    private String phonenumber;

    /** The password of the user */
    @Column(name="password")
    private String password;

    /** Boolean if the user is also a company */
    @Column(name="is_company")
    private boolean isCompany;

    @Column(name="enabled")
    private boolean enabled;

    @Column(name="register_token")
    private String registerToken;

    @Column(name="companyname")
    private String companyName;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User_Location> locations = new ArrayList<>();

    /** Empty Constructor */
    public Users() {}

    /** Constructor */
    public Users(Integer id, String firstname, String lastname, String email, String phonenumber, String password, boolean isCompany, boolean enabled) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        this.isCompany = isCompany;
        this.enabled = enabled;
    }

    public Users(String firstname, String lastname, String email, String phonenumber, String password, boolean isCompany, String companyName) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
        this.isCompany = isCompany;
        this.companyName = companyName;
    }

    /** Getter for id  */
    public Integer getId() {
        return id;
    }
    /** Setter for id */
    public void setId(Integer id) {
        this.id = id;
    }

    /** Getter for firstname */
    public String getFirstname() {
        return firstname;
    }

    /** Setter for firstname */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /** Getter for lastname */
    public String getLastname() {
        return lastname;
    }

    /** Setter for lastname */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /** Getter for Email */
    public String getEmail() {
        return email;
    }

    /** Setter for email */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Getter for phonenumber */
    public String getPhonenumber() {
        return phonenumber;
    }

    /** Setter for phonenumber */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    /** Getter for  password */
    public String getPassword() {
        return password;
    }

    /** Setter for password */
    public void setPassword(String password) {
        this.password = password;
    }

    public List<User_Location> getLocations() {
        return locations;
    }

    public void setLocations(List<User_Location> locations) {
        this.locations = locations;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public void setCompany(boolean company) {
        isCompany = company;
    }

    public String getRegisterToken() {
        return registerToken;
    }

    public void setRegisterToken(String registerToken) {
        this.registerToken = registerToken;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.firstname + " " + this.lastname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", isCompany=" + isCompany +
                ", enabled=" + enabled +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
