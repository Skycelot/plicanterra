package ru.petrosoft.erratum.security.login;

import java.security.Principal;
import java.security.acl.Group;
import javax.naming.InitialContext;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.AbstractServerLoginModule;
import ru.petrosoft.erratum.security.core.ClientSession;
import ru.petrosoft.erratum.security.service.SessionService;

/**
 *
 */
public class WildflyLoginModule extends AbstractServerLoginModule {

    private ClientSession sessionInfo;

    @Override
    public boolean login() throws LoginException {
        try {
            NameCallback nameCallback = new NameCallback("username");
            PasswordCallback passwordCallback = new PasswordCallback("password", false);
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
            String login = nameCallback.getName();
            String sessionId = new String(passwordCallback.getPassword());
            SessionService sessionService = InitialContext.doLookup("java:global/erratum/SessionService");
            ClientSession sessionInfo = sessionService.findSession((String) sessionId);
            if (sessionInfo != null && sessionInfo.principal.getName().equals(login)) {
                this.sessionInfo = sessionInfo;
            }
        } catch (Exception e) {
            throw new LoginException(e.toString());
        }
        return loginOk = this.sessionInfo != null;
    }

    @Override
    protected Principal getIdentity() {
        return sessionInfo.principal;
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        Group result = new SimpleGroup("Roles");
        for (String role : sessionInfo.roles) {
            result.addMember(new SimplePrincipal(role));
        }
        return new Group[]{result};
    }
}
