package org.example.backend.repositories.articleactivity;

import org.example.backend.entities.Activity;
import org.example.backend.entities.Article;
import org.example.backend.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlArticleActivityRepository extends MySqlAbstractRepository implements ArticleActivityRepository{
    @Override
    public List<Long> getActivitiesByArticleId(Long articleId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Long> activityIds = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT act_id FROM articleactivity WHERE art_id = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, articleId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long activityId = resultSet.getLong("act_id");
                activityIds.add(activityId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return activityIds;
    }


    @Override
    public List<Long> getArticlesByActivityId(Long activityId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Long> articleIds = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT art_id FROM articleactivity WHERE act_id = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, activityId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long articleId = resultSet.getLong("art_id");
                articleIds.add(articleId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return articleIds;
    }

}
