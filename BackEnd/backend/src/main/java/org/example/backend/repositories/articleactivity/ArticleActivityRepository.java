package org.example.backend.repositories.articleactivity;

import org.example.backend.entities.Activity;
import org.example.backend.entities.Article;

import java.util.List;

public interface ArticleActivityRepository {

    List<Long> getActivitiesByArticleId(Long articleId);
    List<Long> getArticlesByActivityId(Long activityId);

}
