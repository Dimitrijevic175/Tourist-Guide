package org.example.backend.repositories.comment;

import org.example.backend.entities.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> allComments();

    Comment addComment(Comment comment);

     List<Comment> commentsByArticleId(long articleId);

     Comment getCommentById(Long commentId);

}
