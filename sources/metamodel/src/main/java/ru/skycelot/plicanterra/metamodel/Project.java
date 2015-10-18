package ru.skycelot.plicanterra.metamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import ru.skycelot.plicanterra.util.Argument;
import ru.skycelot.plicanterra.util.ArgumentsChecker;

/**
 *
 */
public class Project {

    public Long id;
    public String code;
    public String name;
    public String description;
    public Map<String, Template> templates;
    public Map<String, Role> roles;

    public static Project loadProject(Connection connection, String projectCode) {
        ArgumentsChecker.notNull(new Argument("connection", connection), new Argument("projectCode", projectCode));
        String templatesQuery = "select p.ID, p.NAME, p.DESCRIPTION from PROJECT p where p.CODE = ?";
        try (PreparedStatement statement = connection.prepareStatement(templatesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, projectCode);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Project result = new Project();
                result.id = resultSet.getLong("ID");
                result.code = projectCode;
                result.name = resultSet.getString("NAME");
                result.description = resultSet.getString("DESCRIPTION");
                return result;
            } else {
                throw new IllegalArgumentException("There is no project{code=" + projectCode + "}");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void gatherProjectTemplates(Map<Long, Template> templates) {
        ArgumentsChecker.notNull(new Argument("templates", templates));
        this.templates = templates.isEmpty() ? new HashMap<>() : new HashMap<>(templates.size() * 2);
        for (Template template : templates.values()) {
            if (this.templates.containsKey(template.code)) {
                throw new IllegalStateException("Duplicate templates with the code {" + template.code + "}!");
            }
            this.templates.put(template.code, template);
        }
    }

    public void gatherProjectRoles(Map<Long, Role> roles) {
        ArgumentsChecker.notNull(new Argument("roles", roles));
        this.roles = roles.isEmpty() ? new HashMap<>() : new HashMap<>(roles.size() * 2);
        for (Role role : roles.values()) {
            if (this.roles.containsKey(role.code)) {
                throw new IllegalStateException("Duplicate roles with the code {" + role.code + "}!");
            }
            this.roles.put(role.code, role);
        }
    }

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Project) {
            final Project other = (Project) obj;
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
        return "Project{" + "code=" + code + '}';
    }
}
