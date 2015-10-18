package ru.skycelot.plicanterra.security.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import ru.skycelot.plicanterra.metamodel.Profile;
import ru.skycelot.plicanterra.metamodel.Project;
import ru.skycelot.plicanterra.metamodel.Role;
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

    private Map<String, User> users;

    @Resource(lookup = "java:/jdbc/metamodel")
    DataSource metamodel;

    @EJB(lookup = "java:global/erratum/ApplicationPropertiesBean")
    ApplicationPropertiesBean properties;

    @PostConstruct
    public void init() {
        try (Connection connection = metamodel.getConnection()) {
            Project project = Project.loadProject(connection, properties.getApplicationCode());
            Map<Long, User> users = User.loadUsers(connection, project.id);
            Map<Long, Profile> profiles = Profile.loadProfiles(connection, project, users);
            Map<Long, Role> roles = Role.loadRoles(connection, project);
            Profile.loadUserRoles(connection, project.id, profiles, roles);
            User.gatherUserProfiles(profiles);
            project.gatherProjectRoles(roles);
            this.users = users.isEmpty() ? new HashMap<>() : new HashMap<>(users.size() * 2);
            for (User user : users.values()) {
                this.users.put(user.login, user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
