package me.bedaring.imsproject.models;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Entity
@DynamicUpdate(value = true)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Size(min=1, message = "Field cannot be empty")
    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    @Transient
    @Size(min=1, message = "Field cannot be empty")
    private String newPassword;

    @Transient
    @Size(min=1, message = "Field cannot be empty")
    private String verifyPassword;

    @ManyToOne
    private AssignedGroup groupId;

    @ManyToOne
    private Carrier carrierId;

    private int active;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "roles_role_id"))
    private Set<Role> roles;


    public User() {
    }

    public User(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.carrierId = user.getCarrierId();
        this.active = user.getActive();
        this.roles = user.getRoles();
        this.id = user.getId();
        this.groupId = user.getGroupId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Carrier getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Carrier carrierId) {
        this.carrierId = carrierId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public AssignedGroup getGroupId() {
        return groupId;
    }

    public void setGroupId(AssignedGroup groupId) {
        this.groupId = groupId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public static String createRandomPassword(int length){
        // create hashed password
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String password = RandomStringUtils.random(length, characters);

        System.out.println(password);

        System.out.println(BCrypt.hashpw(password, BCrypt.gensalt()));


        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                getActive() == user.getActive() &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getPhone(), user.getPhone()) &&
                Objects.equals(getNewPassword(), user.getNewPassword()) &&
                Objects.equals(getVerifyPassword(), user.getVerifyPassword()) &&
                Objects.equals(getGroupId(), user.getGroupId()) &&
                Objects.equals(getCarrierId(), user.getCarrierId()) &&
                Objects.equals(getRoles(), user.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword(), getFirstName(), getLastName(), getEmail(), getPhone(), getNewPassword(), getVerifyPassword(), getGroupId(), getCarrierId(), getActive(), getRoles());
    }
}
