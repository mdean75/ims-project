package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 1, message = "Name cannot be empty")
    private String categoryName;

    @NotNull
    @Size(min = 1, message = "Type cannot be empty")
    private String categoryType;

    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Ticket> catTickets = new ArrayList<>();

    public Category() {
    }

    public Category(@NotNull String categoryName, @NotNull String categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public List<Ticket> getCatTickets() {
        return catTickets;
    }

    public void setCatTickets(List<Ticket> catTickets) {
        this.catTickets = catTickets;
    }
}
