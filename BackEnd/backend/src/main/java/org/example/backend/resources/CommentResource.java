package org.example.backend.resources;

import org.example.backend.entities.Comment;
import org.example.backend.services.CommentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/comments")
public class CommentResource {
    @Inject
    private CommentService commentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> allComments() {
        return commentService.allComments();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Comment addComment(Comment comment) {
        return commentService.addComment(comment);
    }

    @GET
    @Path("/article/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> commentsByArticleId(@PathParam("articleId") long articleId) {
        return commentService.commentsByArticleId(articleId);
    }

    @GET
    @Path("/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Comment getCommentById(@PathParam("commentId") Long commentId) {
        return commentService.getCommentById(commentId);
    }

}
