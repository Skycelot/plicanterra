package ru.skycelot.plicanterra.crud.persist;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class UpdateQuery extends Query {

    private final String tableName;
    private final Long rowPk;
    private final List<String> columns = new LinkedList<>();
    private final List<String> values = new LinkedList<>();

    public UpdateQuery(String tableName, Long rowPk) {
        this.tableName = tableName.toUpperCase();
        this.rowPk = rowPk;
    }

    public void addInlineColumn(String columnName, Object value) {
        columns.add(columnName);
        values.add(value.toString());
    }

    public void addParameterColumn(String columnName, String value) {
        columns.add(columnName);
        values.add("?");
        parameters.add(value);
    }

    @Override
    public String toString() {
        StringBuilder queryBody = new StringBuilder();
        queryBody.append("update ");
        queryBody.append(tableName);
        queryBody.append(" set ");
        int counter = 0;
        while (counter < columns.size()) {
            queryBody.append(columns.get(counter));
            queryBody.append("=");
            queryBody.append(values.get(counter));
            if (counter < columns.size() - 1) {
                queryBody.append(", ");
            }
        }
        queryBody.append(" where ID=");
        queryBody.append(rowPk);
        return queryBody.toString();
    }
}
