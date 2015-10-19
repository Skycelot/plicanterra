package ru.skycelot.plicanterra.crud.persist;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public abstract class Query {

    protected final List<String> parameters = new LinkedList<>();

    public List<String> getParameters() {
        return parameters;
    }
}
