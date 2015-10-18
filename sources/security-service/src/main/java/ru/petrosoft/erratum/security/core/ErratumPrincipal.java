package ru.petrosoft.erratum.security.core;

import java.security.Principal;

/**
 *
 */
public class ErratumPrincipal implements Principal {

    private String login;
    private String sessionId;

    public ErratumPrincipal(String login) {
        this.login = login;
    }

    @Override
    public String getName() {
        return login;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
