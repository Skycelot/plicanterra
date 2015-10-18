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
public class Role {

    public Long id;
    public String code;
    public String name;
    public String desc;
    public Project project;

    public static Map<Long, Role> loadRoles(Connection connection, Project project) {
        Map<Long, Role> result = null;
        String rolesQuery = "select r.ID, r.CODE, r.NAME, r.DESCRIPTION from ROLE r where r.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(rolesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, project.id);
            ResultSet resultSet = statement.executeQuery();
            boolean notEmpty = resultSet.last();
            if (notEmpty) {
                int count = resultSet.getRow();
                resultSet.beforeFirst();
                result = new HashMap<>(count * 2);
                while (resultSet.next()) {
                    Role role = new Role();
                    role.id = resultSet.getLong("ID");
                    role.code = resultSet.getString("CODE");
                    role.name = resultSet.getString("NAME");
                    role.desc = resultSet.getString("DESCRIPTION");
                    role.project = project;
                    result.put(role.id, role);
                }
            } else {
                result = new HashMap<>();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Role) {
            final Role other = (Role) obj;
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
        return "Role{" + "code=" + code + ", project=" + project + '}';
    }
}
