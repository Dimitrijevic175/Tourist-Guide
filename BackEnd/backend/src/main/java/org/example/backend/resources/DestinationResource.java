package org.example.backend.resources;

import org.example.backend.entities.Destination;
import org.example.backend.services.DestinationService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.annotation.ElementType;
import java.util.List;

@Path("/destinations")
public class DestinationResource {

    @Inject
    private DestinationService destinationService;

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDestinationByName(@PathParam("name") String name) {
        Long destinationId = destinationService.getDestinationIdByName(name);
        if (destinationId != null) {
            return Response.ok(destinationId).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Destination> allDestinations(){
        return this.destinationService.allDestinations();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDestination(Destination destination) {
        try {
            Destination addedDestination = destinationService.addDestination(destination);
            if (addedDestination == null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Destination with the same name already exists.")
                        .build();
            }
            return Response.status(Response.Status.CREATED).entity(addedDestination).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error adding destination: " + e.getMessage())
                    .build();
        }
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDestination(@PathParam("id") Long id, Destination destination) {
        destination.setId(id); // Ensure the ID from the path is set in the destination object
        Destination updatedDestination = destinationService.updateDestination(destination);
        if (updatedDestination != null) {
            return Response.ok(updatedDestination).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/delete/{destinationID}")
    public Response deleteDestination(@PathParam("destinationID") Long destinationID) {
        try {
            destinationService.deleteDestination(destinationID);
            return Response.noContent().build(); // HTTP 204 No Content
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting destination: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDestinationById(@PathParam("id") Long id) {
        Destination destination = destinationService.getDestinationById(id);
        if (destination != null) {
            return Response.ok(destination).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
