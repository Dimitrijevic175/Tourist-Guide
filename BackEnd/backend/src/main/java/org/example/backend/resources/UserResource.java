package org.example.backend.resources;


import org.example.backend.anotations.Authorize;
import org.example.backend.entities.LoginRequest;
import org.example.backend.entities.User;
import org.example.backend.services.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users")
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    @Path("/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUserByEmail(@PathParam("email") String email) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found with email: " + email).build();
        }
    }


    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(@Valid LoginRequest loginRequest)
    {

        if (!userService.isUserActive(loginRequest.getEmail())) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("User account is inactive.")
                    .build();
        }

        Map<String, String> response = new HashMap<>();

        String jwt = this.userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (jwt == null) {
            response.put("message", "These credentials do not match our records");
            return Response.status(422, "Unprocessable Entity").entity(response).build();
        }

        response.put("jwt", jwt);

        return Response.ok(response).build();
    }

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    public User addUser(@Valid User user) {
//        return this.userService.addUser(user);
//    }
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addUser(@Valid User user) {
//        try {
//            User addedUser = this.userService.addUser(user);
//            return Response.ok().entity(addedUser).build();
//        } catch (RuntimeException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("Error adding user: " + e.getMessage())
//                    .build();
//        }
//    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Authorize("admin")
    public Response addUser(@Valid User user) {
        try {
            User addedUser = this.userService.addUser(user);
            if (addedUser != null) {
                return Response.ok().entity(addedUser).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Error adding user: User with the provided email already exists.")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error adding user: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{isActive}")
    @Authorize("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> findUserByStatus(@PathParam("isActive") boolean isActive){
        return this.userService.findUserByStatus(isActive);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authorize("admin")
    public List<User> allUsers() {
        return this.userService.allUsers();
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorize("admin")
    public Response deleteUser(@PathParam("id") Long userId) {
        userService.deleteUser(userId);
        return Response.ok("User with ID " + userId + " deleted successfully").build();
    }

    @PUT
    @Path("/{userId}/{isActive}")
    @Authorize("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStatus(@PathParam("userId") Long userId, @PathParam("isActive") boolean isActive) {
        User updatedUser = userService.updateStatus(userId, isActive);
        if (updatedUser != null) {
            return Response.ok(updatedUser).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @GET
    @Path("/id/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUserById(@PathParam("userId") Long userId) {
        User user = userService.findUserById(userId);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/update/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authorize("admin")
    public Response updateUser(@PathParam("userId") Long userId, User user) {
        user.setId(userId);
        User updatedUser = userService.updateUser(user);
        if (updatedUser != null) {
            return Response.ok(updatedUser).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
