package ru.skycelot.plicanterra.metamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.skycelot.plicanterra.util.Argument;
import ru.skycelot.plicanterra.util.ArgumentsChecker;

/**
 *
 */
public class Profile {

    public Long id;
    public User user;
    public Project project;
    public Set<Role> roles;

    public static Map<Long, Profile> loadProfiles(Connection connection, Project project, Map<Long, User> users) {
        String templatesQuery = "select p.ID, p.PRINCIPAL_ID from PROFILE p where p.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(templatesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, project.id);
            ResultSet resultSet = statement.executeQuery();
            Map<Long, Profile> result;
            boolean notEmpty = resultSet.last();
            if (notEmpty) {
                int count = resultSet.getRow();
                resultSet.beforeFirst();
                result = new HashMap<>((int) (count / 0.75) + 100);
                while (resultSet.next()) {
                    Profile profile = new Profile();
                    profile.id = resultSet.getLong("ID");
                    long userId = resultSet.getLong("PRINCIPAL_ID");
                    if (users.containsKey(userId)) {
                        profile.user = users.get(userId);
                        profile.project = project;
                        result.put(profile.id, profile);
                    } else {
                        throw new IllegalStateException("There is no user{id=" + userId + "}");
                    }
                }
            } else {
                result = new HashMap<>();
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadUserRoles(Connection connection, Long projectId, Map<Long, Profile> profiles, Map<Long, Role> roles) {
        ArgumentsChecker.notNull(new Argument("connection", connection), new Argument("projectId", projectId));
        String userRolesQuery = "select pr.PROFILE_ID, pr.ROLE_ID from PRINCIPAL_ROLES pr inner join PROFILE p on pr.PROFILE_ID = p.ID where p.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(userRolesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long profileId = resultSet.getLong("PROFILE_ID");
                if (profiles.containsKey(profileId)) {
                    Profile profile = profiles.get(profileId);
                    long roleId = resultSet.getLong("ROLE_ID");
                    if (roles.containsKey(roleId)) {
                        Role role = roles.get(roleId);
                        if (profile.roles == null) {
                            profile.roles = new HashSet<>();
                        }
                        profile.roles.add(role);
                    } else {
                        throw new IllegalStateException("");
                    }
                } else {
                    throw new IllegalStateException("");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Profile) {
            final Profile other = (Profile) obj;
            if (this.id != null) {
                equality = this.id.equals(other.id);
            } else {
                equality = super.equals(obj);
            }
        }
        return equality;
    }

    @Override
    public String toString() {
        return "Profile{" + "id=" + id + ", user=" + user + ", project=" + project + '}';
    }
}
