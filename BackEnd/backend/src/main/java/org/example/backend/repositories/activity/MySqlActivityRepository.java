package org.example.backend.repositories.activity;

import org.example.backend.entities.Activity;
import org.example.backend.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlActivityRepository extends MySqlAbstractRepository implements ActivityRepository {


    @Override
    public List<Activity> allActivities() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Activity> activities = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM activities";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Activity activity = new Activity();
                activity.setId(resultSet.getLong("act_id"));
                activity.setName(resultSet.getString("name"));
                // Add other fields as necessary
                activities.add(activity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return activities;
    }

    @Override
    public Activity getActivityById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Activity activity = null;

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM activities WHERE act_id = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                activity = new Activity();
                activity.setId(resultSet.getLong("act_id"));
                activity.setName(resultSet.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return activity;
    }

    @Override
    public Activity getActivityByName(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Activity activity = null;

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM activities WHERE name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                activity = new Activity();
                activity.setId(resultSet.getLong("act_id"));
                activity.setName(resultSet.getString("name"));
                // Add other fields as necessary
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return activity;
    }


    @Override
    public Activity addActivity(Activity activity) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String[] generatedColumns = {"act_id"};
            String query = "INSERT INTO activities (name) VALUES (?)";
            statement = connection.prepareStatement(query, generatedColumns);
            statement.setString(1, activity.getName());
            // Add other fields as necessary
            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                activity.setId(resultSet.getLong(1));
            }

            System.out.println("Activity added successfully: " + activity);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return activity;
    }



}
