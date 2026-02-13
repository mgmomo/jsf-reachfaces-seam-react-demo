package com.vision.demo.rest;

import com.vision.demo.rest.dto.ErrorDto;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RestExceptionMapper implements ExceptionMapper<Exception> {

    public Response toResponse(Exception e) {
        int status = 500;
        String message = "Internal server error";

        if (e instanceof AuthorizationException) {
            status = 403;
            message = e.getMessage();
        } else if (e instanceof IllegalArgumentException) {
            status = 400;
            message = e.getMessage();
        }

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorDto(status, message))
                .build();
    }
}
