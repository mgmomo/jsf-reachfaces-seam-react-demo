package com.vision.demo.rest;

import com.vision.demo.auth.HeaderAuthFilter;
import com.vision.demo.auth.UserRole;
import com.vision.demo.model.Location;
import com.vision.demo.model.LocationState;
import com.vision.demo.model.Person;
import com.vision.demo.rest.dto.DashboardDto;
import com.vision.demo.rest.dto.LocationDto;
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

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
public class DashboardResource {

    private DataService getDataService() {
        try {
            return (DataService) new InitialContext().lookup("java:module/DataService");
        } catch (NamingException e) {
            throw new RuntimeException("Cannot look up DataService", e);
        }
    }

    private void requireAtLeast(HttpServletRequest request, UserRole required) {
        HttpSession session = request.getSession(false);
        UserRole role = UserRole.GUEST;
        if (session != null) {
            Object r = session.getAttribute(HeaderAuthFilter.SESSION_ROLE);
            if (r instanceof UserRole) {
                role = (UserRole) r;
            }
        }
        if (!role.isAtLeast(required)) {
            throw new AuthorizationException("Access denied. Required role: " + required);
        }
    }

    @GET
    public Response getDashboard(@Context HttpServletRequest request) {
        requireAtLeast(request, UserRole.USER);
        DataService ds = getDataService();

        DashboardDto dto = new DashboardDto();

        long totalPersons = ds.countPersons();
        long totalLocations = ds.countLocations();
        long activeLocations = ds.countActiveLocations();

        dto.setTotalPersons(totalPersons);
        dto.setTotalLocations(totalLocations);
        dto.setActiveLocations(activeLocations);
        dto.setInactiveLocations(totalLocations - activeLocations);
        dto.setPersonsWithNoLocations(ds.countPersonsWithNoLocations());

        if (totalPersons > 0) {
            // Calculate average locations per person from the person list
            List<Person> allPersons = ds.findAllPersons();
            long totalAssignments = 0;
            for (Person p : allPersons) {
                totalAssignments += p.getLocations().size();
            }
            dto.setAvgLocationsPerPerson(Math.round((double) totalAssignments / totalPersons * 10.0) / 10.0);
        } else {
            dto.setAvgLocationsPerPerson(0);
        }

        // Location state distribution (for pie chart)
        List<DashboardDto.StateCount> stateDist = new ArrayList<DashboardDto.StateCount>();
        stateDist.add(new DashboardDto.StateCount(LocationState.ACTIVE.name(), LocationState.ACTIVE.getLabel(), activeLocations));
        stateDist.add(new DashboardDto.StateCount(LocationState.NOT_ACTIVE.name(), LocationState.NOT_ACTIVE.getLabel(), totalLocations - activeLocations));
        dto.setLocationStateDistribution(stateDist);

        // Persons per location (for bar chart)
        List<Object[]> locationCounts = ds.findLocationPersonCounts();
        List<DashboardDto.LocationPersonCount> personsPerLoc = new ArrayList<DashboardDto.LocationPersonCount>();
        for (Object[] row : locationCounts) {
            personsPerLoc.add(new DashboardDto.LocationPersonCount((String) row[0], (Long) row[1]));
        }
        dto.setPersonsPerLocation(personsPerLoc);

        // Recent persons
        List<Person> recentPersons = ds.findRecentPersons(5);
        List<PersonDto> recentPersonDtos = new ArrayList<PersonDto>();
        for (Person p : recentPersons) {
            recentPersonDtos.add(PersonDto.fromEntity(p));
        }
        dto.setRecentPersons(recentPersonDtos);

        // Recent locations
        List<Location> recentLocations = ds.findRecentLocations(5);
        List<LocationDto> recentLocationDtos = new ArrayList<LocationDto>();
        for (Location l : recentLocations) {
            recentLocationDtos.add(LocationDto.fromEntity(l));
        }
        dto.setRecentLocations(recentLocationDtos);

        return Response.ok(dto).build();
    }
}
