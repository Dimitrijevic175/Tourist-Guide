package org.example.backend.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Article {

    private Long id;

    private String activities;
    private List<String> activityList = new ArrayList<>();

    @NotNull(message = "Title cannot be null.")
    @NotEmpty(message = "Title is required.")
    private String title;
    @NotNull(message = "Text cannot be null.")
    @NotEmpty(message = "Text is required.")
    private String text;
    @NotNull(message = "CreatedAt cannot be null.")
    @NotEmpty(message = "CreatedAt is required.")
    private Date createdAt;
    @NotNull(message = "Views cannot be null.")
    @NotEmpty(message = "Views is required.")
    private int views;
    @NotNull(message = "Author cannot be null.")
    @NotEmpty(message = "Author is required.")
    private Long authorID; //userID
    @NotNull(message = "Destination cannot be null.")
    @NotEmpty(message = "Destination is required.")
    private Long destinationID;

    public Article() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public List<String> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<String> activityList) {
        this.activityList = activityList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Long authorID) {
        this.authorID = authorID;
    }

    public Long getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(Long destinationID) {
        this.destinationID = destinationID;
    }
}
