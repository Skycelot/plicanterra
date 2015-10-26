package ru.skycelot.plicanterra.util.orm.loader;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.persistence.Table;

/**
 *
 */
public class QueryFactory {

    private final Class<?> queryClass;
    private final StringBuilder query;
    private final List parameters;

    public QueryFactory(Class<?> queryClass) {
        if (queryClass.isAnnotationPresent(Table.class)) {
            this.queryClass = queryClass;
            this.query = new StringBuilder();
            this.parameters = new LinkedList();
        } else {
            throw new IllegalArgumentException("Class " + queryClass.getCanonicalName() + " doesn't annotated with javax.persistence.Table");
        }
    }

    public String generateQuery() {
        String tableName = queryClass.getAnnotation(Table.class).name();
        for (Field field : queryClass.getDeclaredFields()) {
            
        }
        return query.toString();
    }

    public Consumer<PreparedStatement> getParametersSetter() {
        return (PreparedStatement statement) -> {
            try {
                int index = 1;
                for (Object parameter : parameters) {
                    if (parameter instanceof Long) {
                        statement.setLong(index, (Long) parameter);
                    } else if (parameter instanceof String) {
                        statement.setString(index, (String) parameter);
                    } else if (parameter instanceof Boolean) {
                        statement.setBoolean(index, (Boolean) parameter);
                    } else {
                        throw new IllegalStateException("Unsupported parameter type: " + parameter.getClass().getCanonicalName());
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
