package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AssignedGroup {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 1, message = "Required field")
    private String groupName;

    @OneToMany
    @JoinColumn(name = "group_id")
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<User> groupUsers = new ArrayList<>();

    @Transient
    private List<AssignedGroup> allGroups = new ArrayList<>();

    public AssignedGroup() {
    }

    public AssignedGroup(String groupName) {
        this.groupName = groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id; }

    public String getGroupName() {
        return groupName;
    }

    public List<AssignedGroup> returnGroups() {
        return allGroups;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<User> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<User> groupUsers) {
        this.groupUsers = groupUsers;
    }
}

