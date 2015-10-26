package ru.skycelot.plicanterra.util.orm.loader;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.sql.DataSource;

/**
 *
 */
public class EntityManager {

    private final Connection connection;
    private final Map<Class, Map> cache = new HashMap<>();

    public EntityManager(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T find(Class<T> entityClass, Object id) {
        OrmReflectionHelper.checkTableAnnotation(entityClass);
        Field idField = OrmReflectionHelper.findIdField(entityClass);
        StringBuilder query = new StringBuilder();
        query.append("SELECT ").append(idField.getAnnotation(Column.class).name());
        query.append(" FROM ").append(entityClass.getAnnotation(Table.class).name());
        query.append(" WHERE ").append(idField.getAnnotation(Column.class).name()).append(" = ").append(id);
        StatementExecutor<T> executor = new StatementExecutor<>(connection, query.toString());
        List<T> queryResult = executor.execute((resultSet) -> {
            try {
                T result = entityClass.newInstance();
                OrmReflectionHelper.setFieldValue(idField, result, resultSet.getObject(1));
                return result;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        switch (queryResult.size()) {
            case 0:
                return null;
            case 1:
                return queryResult.get(0);
            default:
                throw new IllegalStateException("There are more than one objects with id " + id);
        }
    }

    public <T> Object findIdByPropertyValue(Class<T> entityClass, String property, Object value) {
        OrmReflectionHelper.checkTableAnnotation(entityClass);
        Field propertyField = OrmReflectionHelper.findFieldByName(entityClass, property);
        Field idField = OrmReflectionHelper.findIdField(entityClass);
        StringBuilder query = new StringBuilder();
        query.append("SELECT ").append(idField.getAnnotation(Column.class).name());
        query.append(" FROM ").append(entityClass.getAnnotation(Table.class).name());
        query.append(" WHERE ").append(propertyField.getAnnotation(Column.class).name()).append(" = ?");
        PreparedStatementExecutor<T> executor = new PreparedStatementExecutor<>(connection, query.toString());
        List<T> queryResult = executor.execute((statement) -> {
            try {
                statement.setObject(1, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, (resultSet) -> {
            try {
                T result = entityClass.newInstance();
                OrmReflectionHelper.setFieldValue(idField, result, resultSet.getObject(1));
                return result;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        switch (queryResult.size()) {
            case 0:
                return null;
            case 1:
                return OrmReflectionHelper.getFieldValue(idField, queryResult.get(0));
            default:
                throw new IllegalStateException("There are more than one objects with property " + property + " equals " + value);
        }
    }
}
