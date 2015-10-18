package ru.skycelot.plicanterra.crud.persist;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 */
public class Database implements Closeable {

    private Connection connection;
    private PreparedStatement nextSequence;
    private PreparedStatement selectInstanceTemplate;
    private PreparedStatement insertInstanceTemplate;

    public Database(DataSource ds) {
        try {
            connection = ds.getConnection();
            nextSequence = connection.prepareStatement("select nextval('SEQ_INSTANCE')");
            selectInstanceTemplate = connection.prepareStatement("select TEMPLATE_CODE from OBJECT_INDEX where INSTANCE_ID = ?");
            insertInstanceTemplate = connection.prepareStatement("insert into OBJECT_INDEX (INSTANCE_ID, TEMPLATE_CODE) values (?,?)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getNextInstanceSequenceValue() {
        try (ResultSet resultSet = nextSequence.executeQuery()) {
            resultSet.next();
            Long result = resultSet.getLong(1);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveTemplateCodeOfInstance(Long instanceId, String templateCode) {
        try {
            insertInstanceTemplate.setLong(1, instanceId);
            insertInstanceTemplate.setString(2, templateCode);
            int count = insertInstanceTemplate.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long findTemplateCodeOfInstance(Long instanceId) {
        try {
            selectInstanceTemplate.setLong(1, instanceId);
            try (ResultSet resultSet = selectInstanceTemplate.executeQuery()) {
                Long result = resultSet.next() ? resultSet.getLong("TEMPLATE_CODE") : null;
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(InsertQuery query) {
        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {
            int orderNumber = 1;
            for (String parameter : query.parameters) {
                statement.setString(orderNumber, parameter);
                orderNumber++;
            }
            int count = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
