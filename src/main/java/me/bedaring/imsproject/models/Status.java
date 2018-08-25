package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael DeAngelo
 * last updated date: August 25, 2018
 * purpose: This class defines the object used to set the status of an incident ticket.
 */
@Entity
public class Status {

    @GeneratedValue
    @Id
    private int id;

    @NotNull
    private String statusName;

    @OneToMany
    @JoinColumn(name = "status_id")
    private List<Ticket> ticketStatus = new ArrayList<>();

    // constructor
    public Status() {
    }

    // follows are the accessor and modifier methods

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public List<Ticket> getTicketStatus() {
        return this.ticketStatus;
    }

    public void setTicketStatus(List<Ticket> ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
}
