package org.example.backend.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ArticleActivity {

    private Long id;
    @NotNull(message = "Article cannot be null.")
    @NotEmpty(message = "Article is required.")
    private Long articleID;
    @NotNull(message = "Activity cannot be null.")
    @NotEmpty(message = "Activity is required.")
    private Long activityID;

    public ArticleActivity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleID() {
        return articleID;
    }

    public void setArticleID(Long articleID) {
        this.articleID = articleID;
    }

    public Long getActivityID() {
        return activityID;
    }

    public void setActivityID(Long activityID) {
        this.activityID = activityID;
    }
}
