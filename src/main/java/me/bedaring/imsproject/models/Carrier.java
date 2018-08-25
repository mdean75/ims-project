package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael DeAngelo
 * last update date: Aug 21, 2018
 * purpose: This class defines the carrier object that is used for the sms messaging
 */
@Entity
public class Carrier {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 1, message = "Name is required")
    private String carrierName;

    @NotNull
    @Size(min = 1, message = "Domain is required")
    private String carrierDomain;

    // constructor
    public Carrier() {
    }

    // following is the accessor and modifier methods

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarrierName() {
        return this.carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierDomain() {
        return this.carrierDomain;
    }

    public void setCarrierDomain(String carrierDomain) {
        this.carrierDomain = carrierDomain;
    }

}
