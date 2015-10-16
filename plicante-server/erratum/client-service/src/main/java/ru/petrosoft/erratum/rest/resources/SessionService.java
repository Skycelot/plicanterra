package ru.petrosoft.erratum.rest.resources;

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
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import ru.petrosoft.erratum.rest.core.ErratumRestException;
import ru.petrosoft.erratum.security.core.LoginException;

/**
 *
 */
@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
public class SessionService {

    private ru.petrosoft.erratum.security.service.SessionService lookupSessionService() {
        try {
            return InitialContext.doLookup("java:global/erratum/SessionService");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    public String aquireToken(@QueryParam("login") String login) {
        try {
            return lookupSessionService().aquireToken(login);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @POST
    public Response startSession(@FormParam("token") String token, @FormParam("encodedToken") String encodedToken) {
        try {
            lookupSessionService().startSession(token, encodedToken);
            NewCookie authCookie = new NewCookie("ErratumCredential", token);
            return Response.ok().cookie(authCookie).build();
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
