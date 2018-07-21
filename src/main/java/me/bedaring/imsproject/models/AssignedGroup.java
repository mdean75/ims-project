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

    public AssignedGroup() {
    }

    public AssignedGroup(String groupName) {
        this.groupName = groupName;
    }

    public int getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


}

