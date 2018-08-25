package me.bedaring.imsproject.models;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * @author Michael DeAngelo
 * last updated date: August 25, 2018
 * purpose: This class is used to implement database based user roles.
 */
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role")
    private String role;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "roles_role_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id"))
    private Set<User> users;

    // constructor
    public Role() {
    }

    // follows are the accessor and modifier methods

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role1 = (Role) o;
        return getRoleId() == role1.getRoleId() &&
                Objects.equals(getRole(), role1.getRole()) &&
                Objects.equals(getUsers(), role1.getUsers());
    }

}
