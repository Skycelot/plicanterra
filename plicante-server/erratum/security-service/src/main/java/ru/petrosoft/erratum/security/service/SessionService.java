package ru.petrosoft.erratum.security.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.xml.bind.DatatypeConverter;
import ru.petrosoft.erratum.metamodel.Role;
import ru.petrosoft.erratum.metamodel.User;
import ru.petrosoft.erratum.security.core.ErratumPrincipal;
import ru.petrosoft.erratum.security.core.LoginException;
import ru.petrosoft.erratum.security.core.Token;
import ru.petrosoft.erratum.security.login.PrincipalInfo;
import ru.petrosoft.erratum.util.AesCipher;
import ru.petrosoft.erratum.util.Argument;
import ru.petrosoft.erratum.util.ArgumentsChecker;

/**
 *
 */
@Singleton
@EJB(name = "java:global/erratum/SessionService", beanInterface = SessionService.class)
public class SessionService {

    @EJB
    private SecurityService metamodel;

    private final Map<String, Token> tokens = new HashMap<>();
    private final Map<String, PrincipalInfo> sessions = new HashMap<>();

    public String aquireToken(String login) {
        ArgumentsChecker.notNull(new Argument("login", login));
        String random = UUID.randomUUID().toString();
        if (metamodel.userExists(login)) {
            Token token = new Token();
            token.login = login;
            token.token = random;
            token.issued = new Date();
            tokens.put(random, token);
        }
        return random;
    }

    public String startSession(String token, String encodedToken) throws LoginException {
        ArgumentsChecker.notNull(new Argument("token", token), new Argument("encodedToken", encodedToken));
        if (tokens.containsKey(token)) {
            Token tokenEntity = tokens.remove(token);
            if (new Date().getTime() - tokenEntity.issued.getTime() < 100000) {
                User user = metamodel.getUser(tokenEntity.login);
                String referenceEncodedToken = AesCipher.cipher(token, user.password);
                if (referenceEncodedToken.equals(encodedToken)) {
                    PrincipalInfo principalInfo = new PrincipalInfo();
                    ErratumPrincipal principal = new ErratumPrincipal(user.login);
                    principal.setFullName(user.fullName);
                    Set<String> roles = new HashSet<>(user.profile.roles.size());
                    for (Role role : user.profile.roles) {
                        roles.add(role.code);
                    }
                    principal.setRoles(roles);
                    sessions.put(token, principalInfo);
                    return token;
                }
            }
        }
        throw new LoginException();
    }

    public PrincipalInfo findSession(String sessionId) {
        PrincipalInfo result = null;
        if (sessions.containsKey(sessionId)) {
            result = sessions.get(sessionId);
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
