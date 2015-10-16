package ru.petrosoft.erratum.security.login;

import java.security.Principal;
import java.security.acl.Group;
import javax.naming.InitialContext;
import javax.security.auth.login.LoginException;
import javax.security.jacc.PolicyContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.jboss.security.SimpleGroup;
import org.jboss.security.auth.spi.AbstractServerLoginModule;
import ru.petrosoft.erratum.security.core.ErratumPrincipal;
import ru.petrosoft.erratum.security.core.RolePrincipal;
import ru.petrosoft.erratum.security.service.SessionService;

/**
 *
 */
public class WildflyLoginModule extends AbstractServerLoginModule {

    private ErratumPrincipal principal;

    @Override
    public boolean login() throws LoginException {
        try {
            HttpServletRequest request = (HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest");
            Cookie credentialCookie = null;
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ErratumCredential")) {
                    credentialCookie = cookie;
                    break;
                }
            }
            if (credentialCookie != null) {
                SessionService sessionService = InitialContext.doLookup("java:global/erratum/SessionService");
                principal = sessionService.findSession(credentialCookie.getValue());
            }
        } catch (Exception e) {
            throw new LoginException(e.toString());
        }
        return loginOk = principal != null;
    }

    @Override
    protected Principal getIdentity() {
        return principal;
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        Group result = new SimpleGroup("Roles");
        for (RolePrincipal role : principal.getRoles()) {
            result.addMember(role);
        }
        return new Group[]{result};
    }
}
