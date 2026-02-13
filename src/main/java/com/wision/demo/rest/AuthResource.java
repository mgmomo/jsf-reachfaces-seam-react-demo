package com.wision.demo.rest;

import com.wision.demo.auth.HeaderAuthFilter;
import com.wision.demo.auth.UserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
public class AuthResource {

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response me(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String username = "anonymous";
        String role = "GUEST";

        if (session != null) {
            Object u = session.getAttribute(HeaderAuthFilter.SESSION_USERNAME);
            Object r = session.getAttribute(HeaderAuthFilter.SESSION_ROLE);
            if (u != null) {
                username = u.toString();
            }
            if (r != null) {
                role = r.toString();
            }
        }

        Map<String, String> result = new HashMap<String, String>();
        result.put("username", username);
        result.put("role", role);
        return Response.ok(result).build();
    }
}
