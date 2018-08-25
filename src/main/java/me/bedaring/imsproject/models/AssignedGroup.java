package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael DeAngelo
 * last update date: Aug 21, 2018
 * purpose: This class defines the group that users are assigned to.  It it used to group users into functional units
 * or teams.
 */
@Entity
public class AssignedGroup {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 1, message = "Required field")
    private String groupName;

    // constructor
    public AssignedGroup() { }

    // follows are the accessor and modifier methods

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}

