package org.example.backend.repositories.article;

import org.example.backend.entities.Article;

import java.util.List;

public interface ArticleRepository {

     Article findById(Long id);
    List<Article> allArticles();
//    List<Article> allArticles(int pageNumber, int pageSize);
    Article addArticle(Article article);
// Article addArticle(String title, String destination, String text, String activities);
    List<Article> findArticleByDestination(Long dest_id);
    Article updateArticle(Article article);
    void deleteArticle(Long art_id);
    List<Article> findLatestArticles();
    List<Article> findMostViewedArticles();
}
