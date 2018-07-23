package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

    @Transient
    private List<Severity> allSeverities = new ArrayList<>();

    public Severity() {
    }

    public Severity(@NotNull String severityName) {
        this.severityName = severityName;
    }

    public int getId() {
        return id;
    }

    public String getSeverityName() {
        return severityName;
    }

    public void setSeverityName(String severityName) {
        this.severityName = severityName;
    }

    public List<Ticket> getTickets() {
        return severityTickets;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Severity> returnGroups() {
        return allSeverities;
    }

    public void setTickets(List<Ticket> tickets) {
        this.severityTickets = tickets;
    }
}
