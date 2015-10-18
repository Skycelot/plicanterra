package ru.skycelot.plicanterra.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 */
@Path("/test")
public class TestService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "Service is up and running";
    }
}
