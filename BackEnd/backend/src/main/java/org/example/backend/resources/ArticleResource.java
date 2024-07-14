package org.example.backend.resources;

import org.example.backend.entities.Article;
import org.example.backend.services.ArticleService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/articles")
public class ArticleResource {

    @Inject
    private ArticleService articleService;

    @GET
    @Path("/{art_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticleById(@PathParam("art_id") Long art_id) {
        Article article = articleService.getArticleById(art_id);
        if (article != null) {
            return Response.ok(article).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response allArticles() {
        List<Article> articles = articleService.allArticles();
        return Response.ok(articles).build();
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response allArticles(@QueryParam("pageNumber") int pageNumber, @QueryParam("pageSize") int pageSize) {
//        List<Article> articles = articleService.allArticles(pageNumber, pageSize);
//        return Response.ok(articles).build();
//    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addArticle(Article article) {
        Article createdArticle = articleService.addArticle(article);
        if (createdArticle != null) {
            return Response.status(Response.Status.CREATED).entity(createdArticle).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/dest/{dest_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findArticleByDestination(@PathParam("dest_id") Long dest_id) {
        List<Article> articles = articleService.findArticleByDestination(dest_id);
        if (articles != null && !articles.isEmpty()) {
            return Response.ok(articles).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateArticle(@PathParam("id") Long id, Article article) {
        article.setId(id); // Ensure the ID from the path is set in the article object
        Article updatedArticle = articleService.updateArticle(article);
        if (updatedArticle != null) {
            return Response.ok(updatedArticle).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/delete/{art_id}")
    public Response deleteArticle(@PathParam("art_id") Long art_id) {
        try {
            articleService.deleteArticle(art_id);
            return Response.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting article: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/latest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestArticles() {
        List<Article> articles = articleService.findLatestArticles();
        return Response.ok(articles).build();
    }

    @GET
    @Path("/most-viewed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMostViewedArticles() {
        List<Article> articles = articleService.findMostViewedArticles();
        return Response.ok(articles).build();
    }

}
