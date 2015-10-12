package ru.petrosoft.erratum.security.service;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import ru.petrosoft.erratum.metamodel.User;

/**
 *
 */
@Singleton
@EJB(name = "java:global/erratum/SessionService", beanInterface = SessionService.class)
public class SessionService {

    private Map<String, User> sessions = new HashMap<>();

    public String aquireToken() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public String startSession(String login, String encodedToken) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public void endSession(String sessionId) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}
