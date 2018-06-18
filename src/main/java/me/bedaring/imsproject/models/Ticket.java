package me.bedaring.imsproject.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

@Entity
public class Ticket {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Category categoryMain;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String categorySub;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String categoryDetail;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String title;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String description;
    @ManyToOne
    private AssignedGroup assignedGroup;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String assignedPerson;
    @ManyToOne
    private Severity severity;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String requestorName;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String requestorPhone;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String requestorEmail;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String location;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String locationDetail;

    public Ticket(Category categoryMain, String categorySub, String categoryDetail, String title,
                  String description, AssignedGroup assignedGroup, String assignedPerson, Severity severity,
                  String requestorName, String requestorPhone, String requestorEmail, String location,
                  String locationDetail) {

        this.categoryMain = categoryMain;
        this.categorySub = categorySub;
        this.categoryDetail = categoryDetail;
        this.title = title;
        this.description = description;
        this.assignedGroup = assignedGroup;
        this.assignedPerson = assignedPerson;
        this.severity = severity;
        this.requestorName = requestorName;
        this.requestorPhone = requestorPhone;
        this.requestorEmail = requestorEmail;
        this.location = location;
        this.locationDetail = locationDetail;
    }

    public Ticket() { }

    public int getId() {
        return id;
    }

    public Category getCategoryMain() {
        return categoryMain;
    }

    public void setCategoryMain(Category categoryMain) {
        this.categoryMain = categoryMain;
    }

    public String getCategorySub() {
        return categorySub;
    }

    public void setCategorySub(String categorySub) {
        this.categorySub = categorySub;
    }

    public String getCategoryDetail() {
        return categoryDetail;
    }

    public void setCategoryDetail(String categoryDetail) {
        this.categoryDetail = categoryDetail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AssignedGroup getAssignedGroup() {
        return assignedGroup;
    }

    public void setAssignedGroup(AssignedGroup assignedGroup) {
        this.assignedGroup = assignedGroup;
    }

    public String getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(String assignedPerson) {
        this.assignedPerson = assignedPerson;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getRequestorPhone() {
        return requestorPhone;
    }

    public void setRequestorPhone(String requestorPhone) {
        this.requestorPhone = requestorPhone;
    }

    public String getRequestorEmail() {
        return requestorEmail;
    }

    public void setRequestorEmail(String requestorEmail) {
        this.requestorEmail = requestorEmail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    public void setAssignedGroup(Optional<AssignedGroup> group) {
        this.assignedGroup = assignedGroup;
    }

    public void setSeverity(Optional<Severity> displaySeverity) {
    }

    public void setCategoryMain(Optional<Category> category) {
    }
}
