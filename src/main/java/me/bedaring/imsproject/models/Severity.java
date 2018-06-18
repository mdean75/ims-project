package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Severity {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String severityName;

    @OneToMany
    @JoinColumn(name = "severity_id")
    private List<Ticket> severityTickets = new ArrayList<>();

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

    public void setTickets(List<Ticket> tickets) {
        this.severityTickets = tickets;
    }
}
