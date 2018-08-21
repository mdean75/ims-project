package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany
    @JoinColumn(name = "carrier_id")
    private List<User> carrierUsers = new ArrayList<>();

    public Carrier() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public List<User> getCarrierUsers() {
        return carrierUsers;
    }

    public void setCarrierUsers(List<User> carrierUsers) {
        this.carrierUsers = carrierUsers;
    }

    public String getCarrierDomain() {
        return carrierDomain;
    }

    public void setCarrierDomain(String carrierDomain) {
        this.carrierDomain = carrierDomain;
    }
}
