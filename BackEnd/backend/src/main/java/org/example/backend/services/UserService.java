package org.example.backend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.apache.commons.codec.digest.DigestUtils;
import org.example.backend.entities.User;
import org.example.backend.repositories.user.UserRepository;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class UserService {

    @Inject
    UserRepository userRepository;

    public boolean hasRole(String token, String requiredRole) {
        String userRole = this.getUserRole(token);
        return userRole != null && userRole.equalsIgnoreCase(requiredRole);  // Poređenje koje ignoriše velika i mala slova
    }


    public boolean isUserActive(String email) {
        User user = this.userRepository.findUser(email);
        return user != null && user.isActive();
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public String login(String email, String password)
    {
        String hashedPassword = DigestUtils.sha256Hex(password);
        System.out.println("Hashed Password: " + hashedPassword);

        User user = this.userRepository.findUser(email);
        if (user == null) {
            System.out.println("User not found");
            return null;
        }

        if (!user.getPassword().equals(hashedPassword)) {
            System.out.println("Passwords do not match");
            return null;
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24*60*60*1000); // One day

        Algorithm algorithm = Algorithm.HMAC256("secret");

        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(email)
                .withClaim("role", user.getRole())
                .sign(algorithm);
    }
    public String getUserRole(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        return jwt.getClaim("role").asString();
    }

    public boolean isAuthorized(String token){
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        String email = jwt.getSubject();
        String role = jwt.getClaim("role").asString();

        User user = this.userRepository.findUser(email);

        if (user == null){
            return false;
        }

        return true;
    }

    public User addUser(User user){
        return this.userRepository.addUser(user);
    }

    public List<User> findUserByStatus(boolean isActive){
        return this.userRepository.findUserByStatus(isActive);
    }

    public List<User> allUsers() {
        return this.userRepository.allUsers();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

    public User updateStatus(Long userId, boolean isActive) {
        return userRepository.updateStatus(userId, isActive);
    }

    public User findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    public User updateUser(User user) {
        return userRepository.updateUser(user);
    }

}
