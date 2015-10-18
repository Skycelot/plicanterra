package ru.petrosoft.erratum.crud.service.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class Sequence {

    public static Long getNextValue(Connection connection, String templateCode) {
        try (Statement statement = connection.createStatement()) {
            Long result = null;

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
