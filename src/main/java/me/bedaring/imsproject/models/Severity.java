package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael DeAngelo
 * last updated date: August 25, 2018
 * purpose: This class defines the object to assign a severity to an incident ticket.
 */
@Entity
public class Severity {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 1, message = "Name cannot be empty")
    private String severityName;

    @OneToMany
    @JoinColumn(name = "severity_id")
    private List<Ticket> severityTickets = new ArrayList<>();

    // constructor
    public Severity() {
    }

    // follows are the accessor and modifier methods

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeverityName() {
        return this.severityName;
    }

    public void setSeverityName(String severityName) {
        this.severityName = severityName;
    }

    public List<Ticket> getTickets() {
        return this.severityTickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.severityTickets = tickets;
    }
}
