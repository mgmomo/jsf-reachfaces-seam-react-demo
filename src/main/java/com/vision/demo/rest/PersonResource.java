package com.vision.demo.rest;

import com.vision.demo.auth.HeaderAuthFilter;
import com.vision.demo.auth.UserRole;
import com.vision.demo.model.Location;
import com.vision.demo.model.Person;
import com.vision.demo.rest.dto.ErrorDto;
import com.vision.demo.rest.dto.PersonDto;
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

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

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
    public Response list(@Context HttpServletRequest request) {
        requireAtLeast(request, UserRole.USER);
        DataService ds = getDataService();
        List<Person> persons = ds.findAllPersons();
        List<PersonDto> dtos = new ArrayList<PersonDto>();
        for (Person p : persons) {
            dtos.add(PersonDto.fromEntity(p));
        }
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@Context HttpServletRequest request, @PathParam("id") Long id) {
        requireAtLeast(request, UserRole.USER);
        DataService ds = getDataService();
        Person person = ds.findPerson(id);
        if (person == null) {
            return Response.status(404)
                    .entity(new ErrorDto(404, "Person not found"))
                    .build();
        }
        return Response.ok(PersonDto.fromEntity(person)).build();
    }

    @POST
    public Response create(@Context HttpServletRequest request, PersonDto dto) {
        requireAtLeast(request, UserRole.ADMIN);
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            return Response.status(400)
                    .entity(new ErrorDto(400, "firstName is required"))
                    .build();
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            return Response.status(400)
                    .entity(new ErrorDto(400, "lastName is required"))
                    .build();
        }
        DataService ds = getDataService();
        Person entity = dto.toEntity();
        entity.setId(null);
        setLocationsFromIds(entity, dto.getLocationIds(), ds);
        Person saved = ds.savePerson(entity);
        Person fetched = ds.findPerson(saved.getId());
        return Response.status(201).entity(PersonDto.fromEntity(fetched)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@Context HttpServletRequest request, @PathParam("id") Long id, PersonDto dto) {
        requireAtLeast(request, UserRole.ADMIN);
        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) {
            return Response.status(400)
                    .entity(new ErrorDto(400, "firstName is required"))
                    .build();
        }
        if (dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            return Response.status(400)
                    .entity(new ErrorDto(400, "lastName is required"))
                    .build();
        }
        DataService ds = getDataService();
        Person existing = ds.findPerson(id);
        if (existing == null) {
            return Response.status(404)
                    .entity(new ErrorDto(404, "Person not found"))
                    .build();
        }
        Person entity = dto.toEntity();
        entity.setId(id);
        setLocationsFromIds(entity, dto.getLocationIds(), ds);
        Person saved = ds.savePerson(entity);
        Person fetched = ds.findPerson(saved.getId());
        return Response.ok(PersonDto.fromEntity(fetched)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@Context HttpServletRequest request, @PathParam("id") Long id) {
        requireAtLeast(request, UserRole.ADMIN);
        DataService ds = getDataService();
        Person existing = ds.findPerson(id);
        if (existing == null) {
            return Response.status(404)
                    .entity(new ErrorDto(404, "Person not found"))
                    .build();
        }
        ds.deletePerson(id);
        return Response.noContent().build();
    }

    private void setLocationsFromIds(Person person, List<Long> locationIds, DataService ds) {
        person.getLocations().clear();
        if (locationIds != null) {
            for (Long locId : locationIds) {
                Location loc = ds.findLocation(locId);
                if (loc != null) {
                    person.getLocations().add(loc);
                }
            }
        }
    }
}
