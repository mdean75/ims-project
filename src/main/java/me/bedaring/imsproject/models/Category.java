package me.bedaring.imsproject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String categoryName;

    @NotNull
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
