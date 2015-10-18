package ru.skycelot.plicanterra.rest.resources;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.skycelot.plicanterra.rest.core.ErratumRestException;
import ru.skycelot.plicanterra.security.core.LoginException;

/**
 *
 */
@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
public class SessionService {

    private ru.skycelot.plicanterra.security.service.SessionService lookupSessionService() {
        try {
            return InitialContext.doLookup("java:global/erratum/SessionService");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    public String challenge(@QueryParam("login") String login) {
        try {
            return lookupSessionService().challenge(login);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @POST
    public void startSession(@FormParam("nonce") String nonce, @FormParam("response") String response) {
        try {
            lookupSessionService().startSession(nonce, response);
        } catch (LoginException e) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @DELETE
    @Path("{sessionId}")
    public void endSession(@PathParam("sessionId") String sessionId) {
        try {
            lookupSessionService().endSession(sessionId);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }
}
