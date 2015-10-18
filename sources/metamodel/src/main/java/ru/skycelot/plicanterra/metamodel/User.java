package ru.skycelot.plicanterra.metamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class User {

    public Long id;
    public String login;
    public String fullName;
    public String password;
    public Profile profile;

    public static Map<Long, User> loadUsers(Connection connection, Long projectId) {
        Map<Long, User> result = null;
        String templatesQuery = "select p.ID, p.LOGIN, p.FULL_NAME, p.PASSWORD from PRINCIPAL p inner join PROFILE pr on p.ID = pr.PRINCIPAL_ID where pr.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(templatesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            boolean notEmpty = resultSet.last();
            if (notEmpty) {
                int count = resultSet.getRow();
                resultSet.beforeFirst();
                result = new HashMap<>(count * 2);
                while (resultSet.next()) {
                    User user = new User();
                    user.id = resultSet.getLong("ID");
                    user.login = resultSet.getString("LOGIN");
                    user.fullName = resultSet.getString("FULL_NAME");
                    user.password = resultSet.getString("PASSWORD");
                    result.put(user.id, user);
                }
            } else {
                result = new HashMap<>();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void gatherUserProfiles(Map<Long, Profile> profiles) {
        for (Profile profile: profiles.values()) {
            profile.user.profile = profile;
        }
    }

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof User) {
            final User other = (User) obj;
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
        return "User{" + "login=" + login + '}';
    }
}
