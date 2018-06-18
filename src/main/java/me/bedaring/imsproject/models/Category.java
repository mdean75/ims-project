package me.bedaring.imsproject.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String categoryName;

    @NotNull
    private String categoryType;


}
