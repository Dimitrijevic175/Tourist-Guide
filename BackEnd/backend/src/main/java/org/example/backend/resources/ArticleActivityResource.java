package org.example.backend.resources;

import org.example.backend.services.ArticleActivityService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.*;
import java.util.List;


@Path("/articleactivity")
public class ArticleActivityResource {

    @Inject
    private ArticleActivityService articleActivityService;

    @GET
    @Path("/articles/{activityId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticlesByActivityId(@PathParam("activityId") Long activityId) {
        List<Long> articleIds = articleActivityService.getArticlesByActivityId(activityId);
        return Response.ok(articleIds).build();
    }

    @GET
    @Path("/activities/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivitiesByArticleId(@PathParam("articleId") Long articleId) {
        List<Long> activityIds = articleActivityService.getActivitiesByArticleId(articleId);
        return Response.ok(activityIds).build();
    }

}
