package ru.skycelot.plicanterra.security.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import ru.skycelot.metamodel.service.MetamodelCrud;
import ru.skycelot.plicanterra.metamodel.User;
import ru.skycelot.plicanterra.properties.ApplicationPropertiesBean;

/**
 *
 */
@Singleton
@Startup
@Lock(LockType.READ)
@EJB(name = "java:global/erratum/SecurityService", beanInterface = UserService.class)
@PermitAll
public class UserService {

    Map<String, User> users;

    @EJB
    MetamodelCrud crud;

    @EJB
    ApplicationPropertiesBean properties;

    @PostConstruct
    public void init() {
        String applicationCode = properties.getApplicationCode();
        List<User> users = crud.loadUsers(applicationCode);
        this.users = users.stream().collect(Collectors.toMap(user -> user.login, user -> user));
    }

    public boolean userExists(String login) {
        return users.containsKey(login);
    }

    public User getUser(String login) {
        User result = null;
        if (users.containsKey(login)) {
            result = users.get(login);
        }
        return result;
    }
}
