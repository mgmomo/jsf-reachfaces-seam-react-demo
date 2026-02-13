package com.vision.demo.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(PersonResource.class);
        classes.add(LocationResource.class);
        classes.add(AuthResource.class);
        classes.add(RestExceptionMapper.class);
        classes.add(JacksonConfig.class);
        return classes;
    }
}
