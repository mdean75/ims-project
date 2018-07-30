package me.bedaring.imsproject.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Carrier {

    @Id
    @GeneratedValue
    private int id;

    private String carrierName;

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
}
