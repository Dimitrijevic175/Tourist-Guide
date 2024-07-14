package org.example.backend.repositories.comment;

import org.example.backend.entities.Comment;
import org.example.backend.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.*;
import java.util.List;

public class MySqlCommentRepository extends MySqlAbstractRepository implements CommentRepository {

    @Override
    public List<Comment> allComments() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Comment> comments = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM comments";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setId(resultSet.getLong("com_id"));
                comment.setAuthorName(resultSet.getString("authorName"));
                comment.setText(resultSet.getString("text"));
                comment.setCreatedAt(resultSet.getDate("createdAt"));
                comment.setArticleID(resultSet.getLong("articleID"));
                comments.add(comment);
            }

            Collections.sort(comments, Comparator.comparing(Comment::getCreatedAt).reversed());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return comments;
    }

    @Override
    public Comment addComment(Comment comment) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String[] generatedColumns = {"com_id"};
            String query = "INSERT INTO comments (authorName, text, createdAt, articleID) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(query, generatedColumns);
            statement.setString(1, comment.getAuthorName());
            statement.setString(2, comment.getText());
            statement.setDate(3, new java.sql.Date(comment.getCreatedAt().getTime()));
            statement.setLong(4, comment.getArticleID());
            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                comment.setId(resultSet.getLong(1));
            }

            System.out.println("Comment added successfully: " + comment);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return comment;
    }

    @Override
    public List<Comment> commentsByArticleId(long articleId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Comment> comments = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM comments WHERE articleID = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, articleId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setId(resultSet.getLong("com_id"));
                comment.setAuthorName(resultSet.getString("authorName"));
                comment.setText(resultSet.getString("text"));
                comment.setCreatedAt(resultSet.getDate("createdAt"));
                comment.setArticleID(resultSet.getLong("articleID"));
                comments.add(comment);
            }

            Collections.sort(comments, Comparator.comparing(Comment::getCreatedAt).reversed());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return comments;
    }

    @Override
    public Comment getCommentById(Long commentId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Comment comment = null;

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM comments WHERE com_id = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, commentId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                comment = new Comment();
                comment.setId(resultSet.getLong("com_id"));
                comment.setAuthorName(resultSet.getString("authorName"));
                comment.setText(resultSet.getString("text"));
                comment.setCreatedAt(resultSet.getDate("createdAt"));
                comment.setArticleID(resultSet.getLong("articleID"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return comment;
    }

}
