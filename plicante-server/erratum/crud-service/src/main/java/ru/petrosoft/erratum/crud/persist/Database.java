package ru.petrosoft.erratum.crud.persist;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import ru.petrosoft.erratum.crud.transfer.Instance;

/**
 *
 */
public class Database implements Closeable {

    private Connection connection;

    public Database(DataSource ds) {
        try {
            connection = ds.getConnection();
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

    public Long getNextSequenceValue(String code) {
        Long result = null;
        String query = "select SEQ_"+code+".nextval from dual";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                result = resultSet.getLong(1);
            } else {
                throw new IllegalStateException("There is no sequence{name=SEQ_" + code + "}");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void insert(Instance instance) {
        InsertQuery query = new InsertQuery(instance);
        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate(query.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
