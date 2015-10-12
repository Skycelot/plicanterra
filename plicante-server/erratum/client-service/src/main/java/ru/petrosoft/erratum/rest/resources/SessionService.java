package ru.petrosoft.erratum.rest.resources;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import ru.petrosoft.erratum.rest.core.ErratumRestException;

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
    public String aquireToken() {
        try {
            return lookupSessionService().aquireToken();
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }
    
    @POST
    public String startSession(String login, String encodedToken) {
        try {
            return lookupSessionService().startSession(login, encodedToken);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }
    
    @DELETE
    public void endSession (String sessionId) {
        try {
            lookupSessionService().endSession(sessionId);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }
}
