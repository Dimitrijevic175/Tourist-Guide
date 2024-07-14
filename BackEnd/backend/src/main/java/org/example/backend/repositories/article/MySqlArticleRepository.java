package org.example.backend.repositories.article;

import org.example.backend.entities.Activity;
import org.example.backend.entities.Article;
import org.example.backend.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlArticleRepository extends MySqlAbstractRepository implements ArticleRepository {

    @Override
    public Article findById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Article article = null;

        try {
            connection = this.newConnection();

            // Inkrementiranje broja poseta
            String updateQuery = "UPDATE articles SET views = views + 1 WHERE art_id = ?";
            statement = connection.prepareStatement(updateQuery);
            statement.setLong(1, id);
            statement.executeUpdate();

            String query = "SELECT * FROM articles WHERE art_id = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                article = new Article();
                article.setId(resultSet.getLong("art_id"));
                article.setTitle(resultSet.getString("title"));
                article.setText(resultSet.getString("text"));
                article.setCreatedAt(resultSet.getDate("createdAt"));
                article.setViews(resultSet.getInt("views"));
                article.setAuthorID(resultSet.getLong("author_id"));
                article.setDestinationID(resultSet.getLong("destination_id"));
                // Assuming activities are stored as a comma-separated string
                article.setActivities(resultSet.getString("activities"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return article;
    }

    @Override
    public List<Article> allArticles() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM articles";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Article article = new Article();
                article.setId(resultSet.getLong("art_id"));
                article.setTitle(resultSet.getString("title"));
                article.setText(resultSet.getString("text"));
                article.setCreatedAt(resultSet.getDate("createdAt"));
                article.setViews(resultSet.getInt("views"));
                article.setAuthorID(resultSet.getLong("author_id"));
                article.setDestinationID(resultSet.getLong("destination_id"));
                // Assuming 'activities' is a comma-separated string in the database
                article.setActivities(resultSet.getString("activities"));

                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return articles;
    }

//    @Override
//    public List<Article> allArticles(int pageNumber, int pageSize) {
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        List<Article> articles = new ArrayList<>();
//
//        try {
//            connection = this.newConnection();
//
//            // Calculate the offset based on page number and page size
//            int offset = (pageNumber - 1) * pageSize;
//
//            // SQL query with pagination
//            String query = "SELECT * FROM articles LIMIT ? OFFSET ?";
//            statement = connection.prepareStatement(query);
//            statement.setInt(1, pageSize);
//            statement.setInt(2, offset);
//            resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                Article article = new Article();
//                article.setId(resultSet.getLong("art_id"));
//                article.setTitle(resultSet.getString("title"));
//                article.setText(resultSet.getString("text"));
//                article.setCreatedAt(resultSet.getDate("createdAt"));
//                article.setViews(resultSet.getInt("views"));
//                article.setAuthorID(resultSet.getLong("author_id"));
//                article.setDestinationID(resultSet.getLong("destination_id"));
//                // Assuming 'activities' is a comma-separated string in the database
//                article.setActivities(resultSet.getString("activities"));
//
//                articles.add(article);
//            }
//
//            // Log fetched articles
//            System.out.println("Fetched articles: " + articles);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            this.closeResultSet(resultSet);
//            this.closeStatement(statement);
//            this.closeConnection(connection);
//        }
//
//        return articles;
//    }



    @Override
    public Article addArticle(Article article) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String[] generatedColumns = {"art_id"};
//            String query = "INSERT INTO articles (title, text, createdAt, views, author_id, destination_id) VALUES (?, ?, ?, ?, ?, ?)";
            String query = "INSERT INTO articles (title, text, createdAt, views, author_id, destination_id, activities) VALUES (?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query, generatedColumns);
            statement.setString(1, article.getTitle());
            statement.setString(2, article.getText());
            statement.setDate(3, article.getCreatedAt());
            statement.setInt(4, article.getViews());
            statement.setLong(5, article.getAuthorID());
            statement.setLong(6, article.getDestinationID());
            statement.setString(7, article.getActivities());

            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                article.setId(resultSet.getLong(1));
            }

//            // Process activities
//            String activities = article.getActivities(); // Assuming activities are stored as a comma-separated string
//            if (activities != null && !activities.isEmpty()) {
//                String[] activityArray = activities.split(",");
//                for (String activityName : activityArray) {
//                    addArticleActivity(connection, article.getId(), activityName.trim());
//                    article.getActivityList().add(activityName);
//                }
//            }

            // Process activities
            String activities = article.getActivities(); // Assuming activities are stored as a comma-separated string
            if (activities != null && !activities.isEmpty()) {
                String[] activityArray = activities.split(",");
                for (String activityName : activityArray) {
                    Long activityId = addArticleActivity(connection, activityName.trim());
                    if (activityId != null) {
                        addArticleActivityRelation(connection, article.getId(), activityId);
                        article.getActivityList().add(activityName);
                    }
                }
            }

            System.out.println("Article added successfully: " + article);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return article;
    }

//    private void addArticleActivity(Connection connection, Long articleId, String activityName) throws SQLException {
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        try {
//            // Provera da li aktivnost sa istim imenom već postoji
//            String checkQuery = "SELECT COUNT(*) FROM activities WHERE name = ?";
//            statement = connection.prepareStatement(checkQuery);
//            statement.setString(1, activityName);
//            resultSet = statement.executeQuery();
//            int count = 0; // Broj redova koji se vraćaju iz upita
//            if (resultSet.next()) {
//                count = resultSet.getInt(1);
//            }
//
//            if (count == 0) {
//                // Ako aktivnost ne postoji, dodajemo je
//                String insertQuery = "INSERT INTO activities (name) VALUES (?)";
//                statement = connection.prepareStatement(insertQuery);
//                statement.setString(1, activityName);
//                statement.executeUpdate();
//            } else {
//                // Ako aktivnost već postoji, ne radimo ništa
//                System.out.println("Activity with name " + activityName + " already exists.");
//            }
//        } finally {
//            this.closeResultSet(resultSet);
//            this.closeStatement(statement);
//        }
//    }

    private Long addArticleActivity(Connection connection, String activityName) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Long activityId = null;
        try {
            // Provera da li aktivnost sa istim imenom već postoji
            String checkQuery = "SELECT act_id FROM activities WHERE name = ?";
            statement = connection.prepareStatement(checkQuery);
            statement.setString(1, activityName);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                activityId = resultSet.getLong("act_id");
            } else {
                // Ako aktivnost ne postoji, dodajemo je
                String insertQuery = "INSERT INTO activities (name) VALUES (?)";
                statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, activityName);
                statement.executeUpdate();
                resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    activityId = resultSet.getLong(1);
                }
            }
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
        }
        return activityId;
    }

    private void addArticleActivityRelation(Connection connection, Long articleId, Long activityId) throws SQLException {
        PreparedStatement statement = null;
        try {
            String query = "INSERT INTO articleactivity (art_id, act_id) VALUES (?, ?)";
            statement = connection.prepareStatement(query);
            statement.setLong(1, articleId);
            statement.setLong(2, activityId);
            statement.executeUpdate();
        } finally {
            this.closeStatement(statement);
        }
    }


//    private void addArticleActivity(Connection connection, Long articleId, String activityName) throws SQLException {
//        PreparedStatement statement = null;
//        try {
//            String query = "INSERT INTO activities (name) VALUES (?)";
//            statement = connection.prepareStatement(query);
//            statement.setString(1, activityName);
//            statement.executeUpdate();
//        } finally {
//            this.closeStatement(statement);
//        }
//    }

    @Override
    public List<Article> findArticleByDestination(Long dest_id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM articles WHERE destination_id = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, dest_id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Article article = new Article();
                article.setId(resultSet.getLong("art_id"));
                article.setTitle(resultSet.getString("title"));
                article.setText(resultSet.getString("text"));
                article.setCreatedAt(resultSet.getDate("createdAt"));
                article.setViews(resultSet.getInt("views"));
                article.setAuthorID(resultSet.getLong("author_id"));
                article.setDestinationID(resultSet.getLong("destination_id"));
                article.setActivities(resultSet.getString("activities"));
                // Assuming activities are stored as a comma-separated string
                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public Article updateArticle(Article article) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = this.newConnection();

            String query = "UPDATE articles SET title = ?, text = ?, destination_id = ?, activities = ?, createdAt = ? WHERE art_id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, article.getTitle());
            statement.setString(2, article.getText());
            statement.setLong(3, article.getDestinationID()); // Koristi ID destinacije
            statement.setString(4, article.getActivities());
            statement.setDate(5, article.getCreatedAt()); // Postavi createdAt polje
            statement.setLong(6, article.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Article updated successfully: " + article);
            } else {
                System.out.println("Article update failed for ID: " + article.getId());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return article;
    }
//@Override
//public Article updateArticle(Article article) {
//    Connection connection = null;
//    PreparedStatement statement = null;
//
//    try {
//        connection = this.newConnection();
//
//        // Provera i dodavanje destinacije ako ne postoji
//        // Provera i dodavanje destinacije po imenu ako ne postoji
//        String destinationName = article.getDestinationName();
//        if (destinationName != null && !destinationExistsByName(connection, destinationName)) {
//            addDestinationByName(connection, destinationName);
//        }
//
//
//        // Provera i dodavanje aktivnosti ako ne postoje
//        String activities = article.getActivities();
//        if (activities != null && !activities.isEmpty()) {
//            String[] activityArray = activities.split(",");
//            for (String activityName : activityArray) {
//                if (!activityExists(connection, activityName.trim())) {
//                    addActivity(connection, activityName.trim());
//                }
//            }
//        }
//
//        String query = "UPDATE articles SET title = ?, text = ?, destination_id = ?, activities = ?, createdAt = ? WHERE art_id = ?";
//        statement = connection.prepareStatement(query);
//        statement.setString(1, article.getTitle());
//        statement.setString(2, article.getText());
//        statement.setLong(3, article.getDestinationID());
//        statement.setString(4, article.getActivities());
//        statement.setDate(5, article.getCreatedAt());
//        statement.setLong(6, article.getId());
//
//        int rowsAffected = statement.executeUpdate();
//        if (rowsAffected > 0) {
//            System.out.println("Article updated successfully: " + article);
//        } else {
//            System.out.println("Article update failed for ID: " + article.getId());
//            return null;
//        }
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        return null;
//    } finally {
//        this.closeStatement(statement);
//        this.closeConnection(connection);
//    }
//
//    return article;
//}
//
//    // Metoda za proveru postojanja destinacije
//    // Metoda za proveru postojanja destinacije po imenu
//    private boolean destinationExistsByName(Connection connection, String destinationName) throws SQLException {
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        try {
//            String query = "SELECT COUNT(*) FROM destinations WHERE name = ?";
//            statement = connection.prepareStatement(query);
//            statement.setString(1, destinationName);
//            resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                int count = resultSet.getInt(1);
//                return count > 0;
//            }
//        } finally {
//            this.closeResultSet(resultSet);
//            this.closeStatement(statement);
//        }
//        return false;
//    }
//
//    // Metoda za dodavanje destinacije po imenu
//    private void addDestinationByName(Connection connection, String destinationName) throws SQLException {
//        PreparedStatement statement = null;
//        try {
//            String query = "INSERT INTO destinations (name) VALUES (?)";
//            statement = connection.prepareStatement(query);
//            statement.setString(1, destinationName);
//            statement.executeUpdate();
//        } finally {
//            this.closeStatement(statement);
//        }
//    }
//
//    // Metoda za proveru postojanja aktivnosti
//    private boolean activityExists(Connection connection, String activityName) throws SQLException {
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//        try {
//            String query = "SELECT COUNT(*) FROM activities WHERE name = ?";
//            statement = connection.prepareStatement(query);
//            statement.setString(1, activityName);
//            resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                int count = resultSet.getInt(1);
//                return count > 0;
//            }
//        } finally {
//            this.closeResultSet(resultSet);
//            this.closeStatement(statement);
//        }
//        return false;
//    }
//
//    // Metoda za dodavanje aktivnosti
//    private void addActivity(Connection connection, String activityName) throws SQLException {
//        PreparedStatement statement = null;
//        try {
//            String query = "INSERT INTO activities (name) VALUES (?)";
//            statement = connection.prepareStatement(query);
//            statement.setString(1, activityName);
//            statement.executeUpdate();
//        } finally {
//            this.closeStatement(statement);
//        }
//    }

//    @Override
//    public void deleteArticle(Long art_id) {
//        Connection connection = null;
//        PreparedStatement statement = null;
//
//        try {
//            connection = this.newConnection();
//
//            String query = "DELETE FROM articles WHERE art_id = ?";
//            statement = connection.prepareStatement(query);
//            statement.setLong(1, art_id);
//
//            int rowsAffected = statement.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Article deleted successfully. Article ID: " + art_id);
//            } else {
//                System.out.println("Article deletion failed. Article ID: " + art_id);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            this.closeStatement(statement);
//            this.closeConnection(connection);
//        }
//    }

    @Override
    public void deleteArticle(Long art_id) {
        Connection connection = null;
        PreparedStatement articleStatement = null;
        PreparedStatement articleActivityStatement = null;

        try {
            connection = this.newConnection();

            // Delete comments associated with the article
//            String commentQuery = "DELETE FROM comments WHERE articleID = ?";
//            commentStatement = connection.prepareStatement(commentQuery);
//            commentStatement.setLong(1, art_id);
//            int commentRowsAffected = commentStatement.executeUpdate();

            // Delete rows from articleactivity table associated with the article
            String articleActivityQuery = "DELETE FROM articleactivity WHERE art_id = ?";
            articleActivityStatement = connection.prepareStatement(articleActivityQuery);
            articleActivityStatement.setLong(1, art_id);
            int articleActivityRowsAffected = articleActivityStatement.executeUpdate();

            // Delete article
            String query = "DELETE FROM articles WHERE art_id = ?";
            articleStatement = connection.prepareStatement(query);
            articleStatement.setLong(1, art_id);
            int articleRowsAffected = articleStatement.executeUpdate();

            if (articleRowsAffected > 0) {
                System.out.println("Article deleted successfully. Article ID: " + art_id);
            } else {
                System.out.println("Article deletion failed. Article ID: " + art_id);
            }

//            if (commentRowsAffected > 0) {
//                System.out.println("Deleted " + commentRowsAffected + " comments associated with article ID: " + art_id);
//            }

            if (articleActivityRowsAffected > 0) {
                System.out.println("Deleted " + articleActivityRowsAffected + " rows from articleactivity associated with article ID: " + art_id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(articleStatement);
            this.closeConnection(connection);
        }
    }




    @Override
    public List<Article> findLatestArticles() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM articles ORDER BY createdAt DESC LIMIT 10";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Article article = new Article();
                article.setId(resultSet.getLong("art_id"));
                article.setTitle(resultSet.getString("title"));
                article.setText(resultSet.getString("text"));
                article.setCreatedAt(resultSet.getDate("createdAt"));
                article.setViews(resultSet.getInt("views"));
                article.setAuthorID(resultSet.getLong("author_id"));
                article.setDestinationID(resultSet.getLong("destination_id"));
                // Assuming activities are stored as a comma-separated string
                article.setActivities(resultSet.getString("activities"));

                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public List<Article> findMostViewedArticles() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM articles " +
                    "WHERE createdAt >= NOW() - INTERVAL 30 DAY " +
                    "ORDER BY views DESC LIMIT 10";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Article article = new Article();
                article.setId(resultSet.getLong("art_id"));
                article.setTitle(resultSet.getString("title"));
                article.setText(resultSet.getString("text"));
                article.setCreatedAt(resultSet.getDate("createdAt"));
                article.setViews(resultSet.getInt("views"));
                article.setAuthorID(resultSet.getLong("author_id"));
                article.setDestinationID(resultSet.getLong("destination_id"));
                article.setActivities(resultSet.getString("activities")); // Assuming activities are stored as a comma-separated string

                articles.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return articles;
    }
}
