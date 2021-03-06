package ru.skycelot.plicanterra.crud.persist;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class InsertQuery extends Query {

    private final String tableName;
    private final List<String> columns = new LinkedList<>();
    private final List<String> values = new LinkedList<>();

    public InsertQuery(String tableName) {
        this.tableName = tableName.toUpperCase();
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
        queryBody.append("insert into ");
        queryBody.append(tableName);
        queryBody.append(" (");
        Iterator<String> columnsIterator = columns.iterator();
        while (columnsIterator.hasNext()) {
            queryBody.append(columnsIterator.next());
            if (columnsIterator.hasNext()) {
                queryBody.append(", ");
            }
        }
        queryBody.append(") ");
        queryBody.append("values (");
        Iterator<String> valuesIterator = values.iterator();
        while (valuesIterator.hasNext()) {
            queryBody.append(valuesIterator.next());
            if (valuesIterator.hasNext()) {
                queryBody.append(", ");
            }
        }
        queryBody.append(")");
        return queryBody.toString();
    }
}
