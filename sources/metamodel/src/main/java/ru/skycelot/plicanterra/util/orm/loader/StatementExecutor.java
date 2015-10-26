package ru.skycelot.plicanterra.util.orm.loader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 *
 */
public class StatementExecutor<T> {

    private final Connection connection;
    private final String query;

    public StatementExecutor(Connection connection, String query) {
        this.connection = connection;
        this.query = query;
    }

    public List<T> execute(Function<ResultSet, T> extractor) {
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                List<T> result = Collections.emptyList();
                if (resultSet.last()) {
                    int count = resultSet.getRow();
                    result = new ArrayList<>(count);
                    resultSet.beforeFirst();
                    while (resultSet.next()) {
                        result.add(extractor.apply(resultSet));
                    }
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
