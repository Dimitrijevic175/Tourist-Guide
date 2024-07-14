package org.example.backend.repositories.user;

import org.example.backend.entities.User;

import java.util.List;

public interface UserRepository {
     boolean isUserActive(String email);
    public User findUser(String email);
    User addUser(User user);
    List<User> findUserByStatus(boolean isActive);
    List<User> allUsers();
    void deleteUser(Long userId);
    User updateStatus(Long userId, boolean isActive);
    User findUserById(Long userID);
    User findUserByEmail(String email);
    User updateUser(User user);
}
