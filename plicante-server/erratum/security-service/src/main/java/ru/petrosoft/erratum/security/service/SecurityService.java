package ru.petrosoft.erratum.security.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import ru.petrosoft.erratum.metamodel.Profile;
import ru.petrosoft.erratum.metamodel.Project;
import ru.petrosoft.erratum.metamodel.Role;
import ru.petrosoft.erratum.metamodel.User;
import ru.petrosoft.erratum.security.core.ErratumPrincipal;
import ru.petrosoft.erratum.security.core.RolePrincipal;
import ru.petrosoft.newage.propertiesservice.ApplicationPropertiesBean;

/**
 *
 */
@Singleton
@Startup
@Lock(LockType.READ)
@EJB(name = "java:global/erratum/SecurityService", beanInterface = SecurityService.class)
@PermitAll
public class SecurityService {

    private Map<String, User> users;

    @Resource(lookup = "java:/jdbc/metamodel")
    DataSource metamodel;

    @Resource
    EJBContext ctx;

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

    /**
     * Возвращает список ролей, назначенных текущему пользователю.
     *
     * @return - список кодов ролей.
     */
    public List<String> getCurrentUserRoles() {
        List<String> result = Collections.emptyList();
        if (ctx.getCallerPrincipal() instanceof ErratumPrincipal) {
            ErratumPrincipal principal = (ErratumPrincipal) ctx.getCallerPrincipal();
            if (principal.getRoles() != null && !principal.getRoles().isEmpty()) {
                result = new ArrayList<>(principal.getRoles().size());
                for (RolePrincipal role : principal.getRoles()) {
                    result.add(role.getName());
                }
            }
        } else {
            throw new IllegalStateException("Caller principal isn't instance of class ErratumPrincipal!");
        }
        return result;
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
