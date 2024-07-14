package org.example.backend.repositories.destination;

import org.example.backend.entities.Destination;
import org.example.backend.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.*;
import java.util.List;

public class MySqlDestinationRepository extends MySqlAbstractRepository implements DestinationRepository {

    @Override
    public Long getDestinationIdByName(String destinationName) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Long destinationId = null;

        try {
            connection = this.newConnection();

            String query = "SELECT dest_id FROM destinations WHERE name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, destinationName);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                destinationId = resultSet.getLong("dest_id");
            }else {
                // Ako ne postoji destinacija sa datim imenom, vrati null
                destinationId = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return destinationId;
    }


    @Override
    public Destination getDestinationById(Long destinationID) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Destination destination = null;

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM destinations WHERE dest_id = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, destinationID);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                destination = new Destination();
                destination.setId(resultSet.getLong("dest_id"));
                destination.setName(resultSet.getString("name"));
                destination.setDescription(resultSet.getString("description"));
                // Dodati ostale polja ako su potrebna
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return destination;
    }

    @Override
    public List<Destination> allDestinations() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Destination> destinations = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM destinations";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Destination destination = new Destination();
                destination.setId(resultSet.getLong("dest_id"));
                destination.setName(resultSet.getString("name"));
                destination.setDescription(resultSet.getString("description"));
                // Add other fields as necessary
                destinations.add(destination);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return destinations;
    }

    @Override
    public Destination addDestination(Destination destination) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            // Check if a destination with the same name already exists
            String checkQuery = "SELECT COUNT(*) FROM destinations WHERE name = ?";
            statement = connection.prepareStatement(checkQuery);
            statement.setString(1, destination.getName());
            resultSet = statement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // Destination with the same name already exists
                System.out.println("Destination with name '" + destination.getName() + "' already exists.");
                return null;
            }

            // Close the previous statement and resultSet before proceeding with the insert
            this.closeResultSet(resultSet);
            this.closeStatement(statement);

            String[] generatedColumns = {"dest_id"};
            String query = "INSERT INTO destinations (name, description) VALUES (?, ?)";
            statement = connection.prepareStatement(query, generatedColumns);
            statement.setString(1, destination.getName());
            statement.setString(2, destination.getDescription());
            // Add other fields as necessary
            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                destination.setId(resultSet.getLong(1));
            }

            System.out.println("Destination added successfully: " + destination);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return destination;
    }
    @Override
    public Destination updateDestination(Destination destination) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = this.newConnection();

            String query = "UPDATE destinations SET name = ?, description = ? WHERE dest_id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, destination.getName());
            statement.setString(2, destination.getDescription());
            // Add other fields as necessary
            statement.setLong(3, destination.getId());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Destination updated successfully: " + destination);
            } else {
                System.out.println("No destination found with id: " + destination.getId());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return destination;
    }


//    @Override
//    public void deleteDestination(Long destinationID) {
//        Connection connection = null;
//        PreparedStatement checkStatement = null;
//        PreparedStatement deleteStatement = null;
//        ResultSet resultSet = null;
//
//        try {
//            connection = this.newConnection();
//
////            // Check if there are articles for this destination
////            String checkQuery = "SELECT COUNT(*) FROM articles WHERE destination_id = ?";
////            checkStatement = connection.prepareStatement(checkQuery);
////            checkStatement.setLong(1, destinationID);
////            resultSet = checkStatement.executeQuery();
////
////            if (resultSet.next() && resultSet.getInt(1) > 0) {
////                // There are articles for this destination, deletion not allowed
////                System.out.println("Cannot delete destination with ID " + destinationID + " because there are articles associated with it.");
////                return;
////            }
////
////            // Close the previous statement and resultSet before proceeding with the delete
////            this.closeResultSet(resultSet);
////            this.closeStatement(checkStatement);
//
//            // Delete the destination
//            String deleteQuery = "DELETE FROM destinations WHERE dest_id = ?";
//            deleteStatement = connection.prepareStatement(deleteQuery);
//            deleteStatement.setLong(1, destinationID);
//            deleteStatement.executeUpdate();
//
//            System.out.println("Destination deleted successfully: ID " + destinationID);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            this.closeResultSet(resultSet);
//            this.closeStatement(checkStatement);
//            this.closeStatement(deleteStatement);
//            this.closeConnection(connection);
//        }
//    }
@Override
public void deleteDestination(Long destinationID) {
    Connection connection = null;
    PreparedStatement checkStatement = null;
    PreparedStatement deleteStatement = null;
    ResultSet resultSet = null;

    try {
        connection = this.newConnection();

        // Check if there are articles for this destination
        String checkQuery = "SELECT COUNT(*) FROM articles WHERE destination_id = ?";
        checkStatement = connection.prepareStatement(checkQuery);
        checkStatement.setLong(1, destinationID);
        resultSet = checkStatement.executeQuery();

        if (resultSet.next() && resultSet.getInt(1) > 0) {
            // There are articles for this destination, deletion not allowed
            System.out.println("Cannot delete destination with ID " + destinationID + " because there are articles associated with it.");
            return;

        }

        // Close the previous statement and resultSet before proceeding with the delete
        this.closeResultSet(resultSet);
        this.closeStatement(checkStatement);

        // Delete the destination
        String deleteQuery = "DELETE FROM destinations WHERE dest_id = ?";
        deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setLong(1, destinationID);
        deleteStatement.executeUpdate();

        System.out.println("Destination deleted successfully: ID " + destinationID);

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        this.closeResultSet(resultSet);
        this.closeStatement(checkStatement);
        this.closeStatement(deleteStatement);
        this.closeConnection(connection);
    }
}


}
