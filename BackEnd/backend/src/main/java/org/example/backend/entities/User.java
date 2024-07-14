package org.example.backend.entities;

import javax.validation.constraints.*;

public class User {
    private Long id;
    @NotNull(message = "Email cannot be null.")
    @NotEmpty(message = "Email is required.")
    private String email;
    @NotNull(message = "Name cannot be null.")
    @NotEmpty(message = "Name is required.")
    private String name;
    @NotNull(message = "Surname cannot be null.")
    @NotEmpty(message = "Surname is required.")
    private String surname;
    @NotNull(message = "Role cannot be null.")
    @NotEmpty(message = "Role is required.")
    private String role;
    @NotNull(message = "Active cannot be null.")
    private boolean active;
    @NotNull(message = "Password cannot be null.")
    @NotEmpty(message = "Password is required")
    private String password;

    public User(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
