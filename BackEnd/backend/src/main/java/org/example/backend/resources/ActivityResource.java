package org.example.backend.resources;

import org.example.backend.entities.Activity;
import org.example.backend.services.ActivityService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;


@Path("/activity")
public class ActivityResource {
    @Inject
    private ActivityService activityService;

    @GET
    @Path("/name/{name}")
    public Activity getActivityByName(@PathParam("name") String name) {
        return activityService.getActivityByName(name);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response allActivities() {
        List<Activity> activities = activityService.allActivities();
        return Response.ok(activities).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivityById(@PathParam("id") Long id) {
        Activity activity = activityService.getActivityById(id);
        if (activity != null) {
            return Response.ok(activity).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addActivity(Activity activity) {
        Activity addedActivity = activityService.addActivity(activity);
        return Response.ok(addedActivity).build();
    }
}
