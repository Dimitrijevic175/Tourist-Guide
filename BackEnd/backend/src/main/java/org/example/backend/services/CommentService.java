package org.example.backend.services;

import org.example.backend.entities.Comment;
import org.example.backend.repositories.comment.CommentRepository;

import javax.inject.Inject;
import java.util.List;

public class CommentService {
    @Inject
    private CommentRepository commentRepository;
    public List<Comment> allComments() {
        return commentRepository.allComments();
    }

    public Comment addComment(Comment comment) {
        return commentRepository.addComment(comment);
    }

    public List<Comment> commentsByArticleId(long articleId) {
        return commentRepository.commentsByArticleId(articleId);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.getCommentById(commentId);
    }

}
