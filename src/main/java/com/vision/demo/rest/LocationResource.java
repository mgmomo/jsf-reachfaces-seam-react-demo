package com.vision.demo.rest;

import com.vision.demo.auth.HeaderAuthFilter;
import com.vision.demo.auth.UserRole;
import com.vision.demo.model.Location;
import com.vision.demo.rest.dto.ErrorDto;
import com.vision.demo.rest.dto.LocationDto;
import com.vision.demo.service.DataService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocationResource {

    private DataService getDataService() {
        try {
            return (DataService) new InitialContext().lookup("java:module/DataService");
        } catch (NamingException e) {
            throw new RuntimeException("Cannot look up DataService", e);
        }
    }

    private UserRole getRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return UserRole.GUEST;
        }
        Object role = session.getAttribute(HeaderAuthFilter.SESSION_ROLE);
        return role instanceof UserRole ? (UserRole) role : UserRole.GUEST;
    }

    private void requireAtLeast(HttpServletRequest request, UserRole required) {
        UserRole role = getRole(request);
        if (!role.isAtLeast(required)) {
            throw new AuthorizationException("Access denied. Required role: " + required);
        }
    }

    @GET
    public Response list(@Context HttpServletRequest request,
                         @QueryParam("activeOnly") @DefaultValue("false") boolean activeOnly) {
        requireAtLeast(request, UserRole.USER);
        DataService ds = getDataService();
        List<Location> entities = activeOnly ? ds.findActiveLocations() : ds.findAllLocations();
        List<LocationDto> dtos = new ArrayList<LocationDto>();
        for (Location loc : entities) {
            dtos.add(LocationDto.fromEntity(loc));
        }
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@Context HttpServletRequest request, @PathParam("id") Long id) {
        requireAtLeast(request, UserRole.USER);
        DataService ds = getDataService();
        Location loc = ds.findLocation(id);
        if (loc == null) {
            return Response.status(404)
                    .entity(new ErrorDto(404, "Location not found"))
                    .build();
        }
        return Response.ok(LocationDto.fromEntity(loc)).build();
    }

    @POST
    public Response create(@Context HttpServletRequest request, LocationDto dto) {
        requireAtLeast(request, UserRole.ADMIN);
        if (dto.getLocationName() == null || dto.getLocationName().trim().isEmpty()) {
            return Response.status(400)
                    .entity(new ErrorDto(400, "locationName is required"))
                    .build();
        }
        DataService ds = getDataService();
        Location entity = dto.toEntity();
        entity.setId(null);
        Location saved = ds.saveLocation(entity);
        return Response.status(201).entity(LocationDto.fromEntity(saved)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@Context HttpServletRequest request, @PathParam("id") Long id, LocationDto dto) {
        requireAtLeast(request, UserRole.ADMIN);
        if (dto.getLocationName() == null || dto.getLocationName().trim().isEmpty()) {
            return Response.status(400)
                    .entity(new ErrorDto(400, "locationName is required"))
                    .build();
        }
        DataService ds = getDataService();
        Location existing = ds.findLocation(id);
        if (existing == null) {
            return Response.status(404)
                    .entity(new ErrorDto(404, "Location not found"))
                    .build();
        }
        dto.applyTo(existing);
        Location saved = ds.saveLocation(existing);
        return Response.ok(LocationDto.fromEntity(saved)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@Context HttpServletRequest request, @PathParam("id") Long id) {
        requireAtLeast(request, UserRole.ADMIN);
        DataService ds = getDataService();
        Location existing = ds.findLocation(id);
        if (existing == null) {
            return Response.status(404)
                    .entity(new ErrorDto(404, "Location not found"))
                    .build();
        }
        ds.deleteLocation(id);
        return Response.noContent().build();
    }
}
