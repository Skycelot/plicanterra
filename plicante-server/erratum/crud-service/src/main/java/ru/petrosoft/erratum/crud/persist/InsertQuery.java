package ru.petrosoft.erratum.crud.persist;

import java.util.LinkedList;
import java.util.List;
import ru.petrosoft.erratum.crud.transfer.Instance;

/**
 *
 */
class InsertQuery {

    StringBuilder queryBody = new StringBuilder();
    List<Object> parameters = new LinkedList<>();

    public InsertQuery(Instance instance) {
        queryBody.append("insert into ");
        queryBody.append(instance.template.code);
        queryBody.append(" (ID, STATUS_ID, VERSION) ");
        queryBody.append("values (");
        queryBody.append(instance.id);
        queryBody.append(',');
        queryBody.append(instance.status.id);
        queryBody.append(',');
        queryBody.append(instance.version);
        queryBody.append(")");
    }

    @Override
    public String toString() {
        return queryBody.toString();
    }
}
