package org.example.backend.filters;


import org.example.backend.anotations.Authorize;
import org.example.backend.resources.*;
import org.example.backend.services.UserService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.List;

@Provider
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    UserService userService;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if(!this.isAuthRequired(requestContext))
            return;

        Method method = resourceInfo.getResourceMethod();
        if (method == null) {
            return;
        }

        Authorize authorize = method.getAnnotation(Authorize.class);
        if (authorize == null) {
            authorize = resourceInfo.getResourceClass().getAnnotation(Authorize.class);
        }

        if (authorize != null) {
            String token = requestContext.getHeaderString("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.replace("Bearer ", "");
            }

            if (!this.userService.isAuthorized(token)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                return;
            }

            String requiredRole = authorize.value().toLowerCase(); // Konvertujemo u mala slova
            if (!this.userService.hasRole(token, requiredRole)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }
        }
    }

//    @Override
//    public void filter(ContainerRequestContext requestContext) throws IOException {
//
//        if (!this.isAuthRequired(requestContext)) {
//            return;
//        }
//
//        try {
//            String token = requestContext.getHeaderString("Authorization");
//            if(token != null && token.startsWith("Bearer ")) {
//                token = token.replace("Bearer ", "");
//            }
//
//            if (!this.userService.isAuthorized(token)) {
//                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//            }
//
//            // Dodata provera za role
//            if (isRoleCheckRequired(requestContext) && !hasRequiredRole(token, requestContext)) {
//                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
//                return;
//            }
//
//        } catch (Exception exception) {
//            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//        }
//    }

    private boolean isRoleCheckRequired(ContainerRequestContext req) {
        List<Object> matchedResources = req.getUriInfo().getMatchedResources();
        for (Object matchedResource : matchedResources) {
            if (matchedResource instanceof UserResource) {
                return true;
            }
        }
        return false;
    }


    private boolean hasRequiredRole(String token, ContainerRequestContext req) {
        String role = this.userService.getUserRole(token);
        List<Object> matchedResources = req.getUriInfo().getMatchedResources();
        for (Object matchedResource : matchedResources) {
            if (matchedResource instanceof UserResource) {
                return "admin".equalsIgnoreCase(role);
            }
        }
        return true;
    }

    private boolean isAuthRequired(ContainerRequestContext req) {
        if (req.getUriInfo().getPath().contains("login")) {
            return false;
        }

        List<Object> matchedResources = req.getUriInfo().getMatchedResources();
        for (Object matchedResource : matchedResources) {
            if (matchedResource instanceof ActivityResource) {
                return true;
            }
            if (matchedResource instanceof ArticleResource) {
                return true;
            }
            if (matchedResource instanceof CommentResource) {
                return true;
            }
            if (matchedResource instanceof DestinationResource) {
                return true;
            }
            if (matchedResource instanceof UserResource) {
                return true;
            }
        }

        return false;
    }
}
