package org.example.backend.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class Destination {

    private Long id;
    @NotNull(message = "Name cannot be null.")
    @NotEmpty(message = "Name is required.")
    private String name;
    @NotNull(message = "Description cannot be null.")
    @NotEmpty(message = "Description is required.")
    private String description;

    public Destination(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
