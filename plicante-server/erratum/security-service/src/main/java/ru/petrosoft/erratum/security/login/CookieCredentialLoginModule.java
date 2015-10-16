package ru.petrosoft.erratum.security.login;

import java.security.Principal;
import java.util.Map;
import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.security.jacc.PolicyContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import ru.petrosoft.erratum.security.core.ErratumPrincipal;
import ru.petrosoft.erratum.security.service.SessionService;

/**
 *
 */
public class CookieCredentialLoginModule implements LoginModule {

    private ErratumPrincipal principal;
    private Subject subject;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
    }

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
        return principal != null;
    }

    @Override
    public boolean commit() throws LoginException {
        boolean result = false;
        if (principal != null) {
            try {
                subject.getPrincipals().add(new Principal() {

                    @Override
                    public String getName() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });
                result = true;
            } catch (Exception e) {
                LoggerFactory.getLogger(CookieCredentialLoginModule.class).error("CookeCredentialLoginModule.commit(): ", e);
            }
        }
        return result;
    }

    @Override
    public boolean abort() throws LoginException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean logout() throws LoginException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
