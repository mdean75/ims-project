package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    public Status() {
    }

    public Status(@NotNull String statusName) {
        this.statusName = statusName;
    }

    public int getId() {
        return id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public List<Ticket> getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(List<Ticket> ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
}
