package ru.skycelot.plicanterra.metamodel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import ru.skycelot.plicanterra.util.Argument;
import ru.skycelot.plicanterra.util.ArgumentsChecker;
import ru.skycelot.plicanterra.util.orm.loader.PreparedStatementExecutor;

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

    public static Project loadProject(Connection connection, String code) {
        ArgumentsChecker.notNull(new Argument("code", code));
        String query = "SELECT id, code, name, description FROM project WHERE code = ?";
        PreparedStatementExecutor<Project> loader = new PreparedStatementExecutor<>(connection, query);
        List<Project> queryResult = loader.execute(statement -> {
            try {
                statement.setString(1, code);
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }, resultSet -> {
            try {
                Project result = new Project();
                result.id = resultSet.getLong("id");
                result.code = resultSet.getString("code");
                result.name = resultSet.getString("name");
                result.description = resultSet.getString("description");
                return result;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        if (queryResult.size() == 1) {
            return queryResult.get(0);
        } else {
            throw new IllegalStateException("There is no project with code " + code + " or there are more than one project with such code!");
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
