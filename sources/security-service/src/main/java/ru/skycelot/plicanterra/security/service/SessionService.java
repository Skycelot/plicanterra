package ru.skycelot.plicanterra.security.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import ru.skycelot.plicanterra.metamodel.User;
import ru.skycelot.plicanterra.security.core.LoginException;
import ru.skycelot.plicanterra.security.core.Challenge;
import ru.skycelot.plicanterra.security.core.ClientSession;
import ru.skycelot.plicanterra.security.core.ErratumPrincipal;
import ru.skycelot.plicanterra.util.AesCipher;
import ru.skycelot.plicanterra.util.Argument;
import ru.skycelot.plicanterra.util.ArgumentsChecker;
import ru.skycelot.plicanterra.properties.ApplicationPropertiesBean;

/**
 *
 */
@Singleton
@Startup
@EJB(name = "java:global/erratum/SessionService", beanInterface = SessionService.class)
@PermitAll
public class SessionService {

    @EJB
    private UserService metamodel;

    @EJB
    private ApplicationPropertiesBean properties;

    @Resource
    EJBContext ctx;

    private final Map<String, Challenge> challenges = new HashMap<>();
    private final Map<String, ClientSession> sessions = new HashMap<>();

    public String challenge(String login) {
        ArgumentsChecker.notNull(new Argument("login", login));
        String nonce = UUID.randomUUID().toString();
        if (metamodel.userExists(login)) {
            Challenge challenge = new Challenge();
            challenge.login = login;
            challenge.nonce = nonce;
            challenge.issued = new Date();
            challenges.put(challenge.nonce, challenge);
        }
        return nonce;
    }

    public void startSession(String nonce, String response) throws LoginException {
        ArgumentsChecker.notNull(new Argument("nonce", nonce), new Argument("response", response));
        if (challenges.containsKey(nonce)) {
            Challenge tokenEntity = challenges.remove(nonce);
            if (new Date().getTime() - tokenEntity.issued.getTime() < 100000) {
                User user = metamodel.getUser(tokenEntity.login);
                String referenceEncodedToken = AesCipher.cipher(nonce, user.password);
                if (referenceEncodedToken.equals(response)) {
                    ClientSession session = new ClientSession();
                    session.principal = new ErratumPrincipal(user.login);
                    session.principal.setSessionId(nonce);
                    session.roles = new ArrayList<>(user.profile.roles.keySet());
                    sessions.put(session.principal.getSessionId(), session);
                    return;
                }
            }
        }
        throw new LoginException();
    }

    public ClientSession findSession(String sessionId) {
        ClientSession result = null;
        if (sessions.containsKey(sessionId)) {
            result = sessions.get(sessionId);
        } else if (properties.isAuthenticationEngaged() && metamodel.userExists(sessionId)) {
            User user = metamodel.getUser(sessionId);
            result = new ClientSession();
            result.principal = new ErratumPrincipal(sessionId);
            result.principal.setSessionId(sessionId);
            result.roles = new ArrayList<>(user.profile.roles.keySet());
            sessions.put(result.principal.getSessionId(), result);
        }
        return result;
    }

    /**
     * Возвращает список ролей, назначенных текущему пользователю.
     *
     * @return - список кодов ролей.
     */
    public List<String> getCurrentPrincipalRoles() {
        List<String> result = Collections.emptyList();
        if (ctx.getCallerPrincipal() instanceof ErratumPrincipal) {
            ErratumPrincipal principal = (ErratumPrincipal) ctx.getCallerPrincipal();
            if (sessions.containsKey(principal.getSessionId())) {
                ClientSession session = sessions.get(principal.getSessionId());
                if (session.roles != null && !session.roles.isEmpty()) {
                    result = new ArrayList<>(session.roles);
                }
            }
        } else {
            throw new IllegalStateException("Caller principal isn't instance of class ErratumPrincipal!");
        }
        return result;
    }

    public void endSession(String sessionId) {
        if (sessions.containsKey(sessionId)) {
            sessions.remove(sessionId);
        } else {
            throw new IllegalStateException("There is no session with id={" + sessionId + "}");
        }
    }
}
