package org.example.backend.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDateTime;

public class Comment {

    private Long id;
    @NotNull(message = "AuthorName cannot be null.")
    @NotEmpty(message = "AuthorName is required.")
    private String authorName;
    @NotNull(message = "Text cannot be null.")
    @NotEmpty(message = "Text is required.")
    private String text;
    @NotNull(message = "CreatedAt cannot be null.")
    @NotEmpty(message = "CreatedAt is required.")
    private Date createdAt;
    @NotNull(message = "Article cannot be null.")
    @NotEmpty(message = "Article is required.")
    private Long articleID;

    public Comment(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getArticleID() {
        return articleID;
    }

    public void setArticleID(Long articleID) {
        this.articleID = articleID;
    }
}
