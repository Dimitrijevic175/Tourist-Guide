package org.example.backend.services;

import org.example.backend.entities.Article;
import org.example.backend.repositories.activity.ActivityRepository;
import org.example.backend.repositories.article.ArticleRepository;

import javax.inject.Inject;
import java.util.List;

public class ArticleService {

    @Inject
    private ArticleRepository articleRepository;

    public Article getArticleById(Long art_id) {
        return articleRepository.findById(art_id);
    }

    public List<Article> allArticles() {
        return articleRepository.allArticles();
    }

//    public List<Article> allArticles(int pageNumber, int pageSize) {
//        return articleRepository.allArticles(pageNumber, pageSize);
//    }

    public Article addArticle(Article article) {
        return articleRepository.addArticle(article);
    }



    public List<Article> findArticleByDestination(Long dest_id) {
        return articleRepository.findArticleByDestination(dest_id);
    }
    public Article updateArticle(Article article) {
        return articleRepository.updateArticle(article);
    }

    public void deleteArticle(Long art_id) {
        articleRepository.deleteArticle(art_id);
    }

    public List<Article> findLatestArticles(){
        return articleRepository.findLatestArticles();
    }

    public List<Article> findMostViewedArticles(){
        return articleRepository.findMostViewedArticles();
    }
}
