package me.bedaring.imsproject.models;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author Michael DeAngelo
 * last updated date: August 25, 2018
 * purpose: This class defines the User object and used to get details about the system users.
 */
@Entity
@DynamicUpdate(value = true)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(nullable = false, unique = true)
    @Size(min = 1, message = "username is required")
    private String username;

    @Size(min=1, message = "Field cannot be empty")
    private String password;

    @NotNull
    @Column(nullable = false, unique = true)
    @Size(min = 1, message = "First name is required")
    private String firstName;

    @NotNull
    @Column(nullable = false, unique = true)
    @Size(min = 1, message = "Last name is required")
    private String lastName;

    @NotNull
    @Column(nullable = false, unique = true)
    @Size(min = 1, message = "Valid email is required")
    @Email(message = "Valid email is required")
    private String email;

    private String phone;

    // only used when user is changing their password, not stored in the database, this is the new password
    @Transient
    @Size(min=8, message = "Password must be at least 8 characters")
    private String newPassword;

    // only used when user is changing their password, not stored in the database, this the to verify the new password
    @Transient
    @Size(min=8, message = "Password must be at least 8 characters")
    private String verifyPassword;

    @ManyToOne
    private AssignedGroup groupId;

    @ManyToOne
    private Carrier carrierId;

    private int active;

    // used to get the roles for the given user
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "roles_role_id"))
    private Set<Role> roles;

    // used to store the new account/reset password token string
    private String token;

    // used to store the expiration time of the token
    private Timestamp tokenExpiration;

    // used to track number of times the toke was used, max of 3 attempts for any given token to complete the action
    // (either completing account set up or resetting the password)
    private int tokenAttempts;


    // default constructor
    public User() {
    }

    // copy constructor used to get details of the current user
    public User(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.groupId = user.getGroupId();
        this.carrierId = user.getCarrierId();
        this.active = user.getActive();
        this.roles = user.getRoles();
    }

    // follows are the accessor and modifier methods

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AssignedGroup getGroupId() {
        return this.groupId;
    }

    public void setGroupId(AssignedGroup groupId) {
        this.groupId = groupId;
    }

    public Carrier getCarrierId() {
        return this.carrierId;
    }

    public void setCarrierId(Carrier carrierId) {
        this.carrierId = carrierId;
    }

    public int getActive() {
        return this.active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerifyPassword() {
        return this.verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Timestamp tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public int getTokenAttempts() {
        return tokenAttempts;
    }

    public void setTokenAttempts(int tokenAttempts) {
        this.tokenAttempts = tokenAttempts;
    }

    /**
     * This method takes an integer length and returns a random password using alpha-numeric characters
     * @param length length of the desired new password
     * @return String the new random password
     */
    public static String createRandomPassword(int length){
        // set the characters to use and create and return the new random password

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return RandomStringUtils.random(length, characters);
    }



}
