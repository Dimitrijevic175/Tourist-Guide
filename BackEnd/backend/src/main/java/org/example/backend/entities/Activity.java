package org.example.backend.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class Activity {

    private Long id;
    @NotNull(message = "Name cannot be null.")
    @NotEmpty(message = "Name is required.")
    private String name;
    private List<Long> articleActivitiesID;

    public Activity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getArticleActivitiesID() {
        return articleActivitiesID;
    }

    public void setArticleActivitiesID(List<Long> articleActivitiesID) {
        this.articleActivitiesID = articleActivitiesID;
    }
}
