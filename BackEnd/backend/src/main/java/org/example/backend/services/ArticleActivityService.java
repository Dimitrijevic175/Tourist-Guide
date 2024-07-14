package org.example.backend.services;

import org.example.backend.repositories.articleactivity.ArticleActivityRepository;

import javax.inject.Inject;
import java.util.List;

public class ArticleActivityService {

    @Inject
    private ArticleActivityRepository articleActivityRepository;

    public List<Long> getActivitiesByArticleId(Long articleId) {
        return articleActivityRepository.getActivitiesByArticleId(articleId);
    }

    public List<Long> getArticlesByActivityId(Long activityId) {
        return articleActivityRepository.getArticlesByActivityId(activityId);
    }

}
