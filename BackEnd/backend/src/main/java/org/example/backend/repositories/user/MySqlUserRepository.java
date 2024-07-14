package org.example.backend.repositories.user;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.backend.entities.User;
import org.example.backend.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MySqlUserRepository extends MySqlAbstractRepository implements UserRepository {

    @Override
    public boolean isUserActive(String email) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean isActive = false;

        try {
            connection = this.newConnection();

            String query = "SELECT active FROM user WHERE email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                isActive = resultSet.getBoolean("active");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return isActive;
    }


    @Override
    public User findUser(String email) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM user WHERE email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                user.setSurname(resultSet.getString("surname"));
                user.setActive(resultSet.getBoolean("active"));
                user.setPassword(resultSet.getString("password"));
                System.out.println("User found: " + user);
            }else {
            System.out.println("User not found with email: " + email);
        }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User addUser(User user) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String checkQuery = "SELECT COUNT(*) FROM user WHERE email = ?";
            statement = connection.prepareStatement(checkQuery);
            statement.setString(1, user.getEmail());
            resultSet = statement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // Email already exists, throw an exception
//                throw new RuntimeException("User with the provided email already exists.");
                return null;
            }

            String[] generatedColumns = {"id"};

            String hashedPassword = DigestUtils.sha256Hex(user.getPassword());

            String query = "INSERT INTO user (name, surname, email, password, role, active) VALUES (?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(query,generatedColumns);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());
            statement.setString(4, hashedPassword); // Ensure the password is already hashed
            statement.setString(5, user.getRole());
            statement.setBoolean(6, user.isActive());
            statement.executeUpdate();

            System.out.println("User added successfully: " + user);
            resultSet =statement.getGeneratedKeys();

            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeConnection(connection);
        }
        return user;
    }

    @Override
    public List<User> findUserByStatus(boolean isActive) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<User> userList = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM user WHERE active = ?";
            statement = connection.prepareStatement(query);
            statement.setBoolean(1, isActive);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                user.setActive(resultSet.getBoolean("active"));
                user.setPassword(resultSet.getString("password"));
                userList.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return userList;
    }

    @Override
    public List<User> allUsers() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<>();

        try {
            connection = this.newConnection();
            String query = "SELECT * FROM user";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                user.setSurname(resultSet.getString("surname"));
                user.setActive(resultSet.getBoolean("active"));
                user.setPassword(resultSet.getString("password"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return users;
    }

    @Override
    public void deleteUser(Long userId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = this.newConnection();

            String query = "DELETE FROM user WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, userId);
            statement.executeUpdate();

            System.out.println("User with ID " + userId + " deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeConnection(connection);
        }
    }

    @Override
    public User updateStatus(Long userId, boolean isActive) {
        Connection connection = null;
        PreparedStatement statement = null;
        User updatedUser = null;

        try {
            connection = this.newConnection();

            String query = "UPDATE user SET active = ? WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setBoolean(1, isActive);
            statement.setLong(2, userId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                updatedUser = findUserById(userId); // Implement this method to fetch the updated user from the database
                System.out.println("User status updated successfully. User ID: " + userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return updatedUser;
    }

    @Override
    public User findUserById(Long userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM user WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setLong(1, userId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                user.setSurname(resultSet.getString("surname"));
                user.setActive(resultSet.getBoolean("active"));
                user.setPassword(resultSet.getString("password"));
                System.out.println("User found: " + user);
            } else {
                System.out.println("User not found with ID: " + userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM user WHERE email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                user.setSurname(resultSet.getString("surname"));
                user.setActive(resultSet.getBoolean("active"));
                user.setPassword(resultSet.getString("password"));
                System.out.println("User found: " + user);
            } else {
                System.out.println("User not found with email: " + email);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return user;
    }


    @Override
    public User updateUser(User user) {
        Connection connection = null;
        PreparedStatement statement = null;
        User updatedUser = null;

        try {
            connection = this.newConnection();

            String query = "UPDATE user SET name = ?, surname = ?, email = ?, role = ? WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getRole());
            statement.setLong(5, user.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                updatedUser = findUserById(user.getId()); // Assuming you have a method to fetch the user by ID
                System.out.println("User updated successfully. User ID: " + user.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return updatedUser;
    }
}
