package me.bedaring.imsproject.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Ticket {

    @Id
    @GeneratedValue
    private int id;
    @CreationTimestamp
    private LocalDateTime createTimestamp;
    @UpdateTimestamp
    private LocalDateTime updateTimestamp;
    @ManyToOne
    private Category categoryMain;
    @ManyToOne
    private Category categorySub;
    @ManyToOne
    private Category categoryDetail;
    @NotNull
    @Size(min=1, message = "Field cannot be empty")
    private String title;
    @NotNull
    @Column(length = 500, columnDefinition = "Text")
    @Size(min=1, max=500, message = "Description must be between 1 and 500 characters")
    private String description;
    @ManyToOne
    private AssignedGroup assignedGroup;
    @ManyToOne
    private User assignedPerson;
    @ManyToOne
    private Severity severity;
    @ManyToOne
    private Status status;
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
    @Column(length = 65535, columnDefinition = "Text")
    private StringBuilder log;
    @Transient
    private String update;

    public Ticket(LocalDateTime createTimestamp, LocalDateTime updateTimestamp, Category categoryMain, Category categorySub, Category categoryDetail, String title,
                  String description, AssignedGroup assignedGroup, User assignedPerson, Severity severity, Status status,
                  String requestorName, String requestorPhone, String requestorEmail, String location,
                  String locationDetail, StringBuilder log) {

        this.categoryMain = categoryMain;
        this.categorySub = categorySub;
        this.categoryDetail = categoryDetail;
        this.title = title;
        this.description = description;
        this.assignedGroup = assignedGroup;
        this.assignedPerson = assignedPerson;
        this.severity = severity;
        this.status = status;
        this.requestorName = requestorName;
        this.requestorPhone = requestorPhone;
        this.requestorEmail = requestorEmail;
        this.location = location;
        this.locationDetail = locationDetail;
        this.createTimestamp = createTimestamp;
        this.updateTimestamp = updateTimestamp;
        this.log = log;
    }

    public Ticket() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(LocalDateTime createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public LocalDateTime getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(LocalDateTime updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Category getCategoryMain() {
        return categoryMain;
    }

    public void setCategoryMain(Category categoryMain) {
        this.categoryMain = categoryMain;
    }

    public Category getCategorySub() {
        return categorySub;
    }

    public void setCategorySub(Category categorySub) {
        this.categorySub = categorySub;
    }

    public Category getCategoryDetail() {
        return categoryDetail;
    }

    public void setCategoryDetail(Category categoryDetail) {
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

    public User getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(User assignedPerson) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(Optional<Status> status) {

    }

    public StringBuilder getLog() {
        return log;
    }

    public void setLog(StringBuilder log) {
        this.log = log;
    }

    public void setCategorySub(Optional<Category> subCategory) {
    }

    public void setCategoryDetail(Optional<Category> detailCategory) {
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setAssignedPerson(Optional<User> user) {

    }

    public void setStatus(int statusId) {

    }
}
