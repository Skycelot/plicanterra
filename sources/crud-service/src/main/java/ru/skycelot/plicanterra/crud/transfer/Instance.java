package ru.skycelot.plicanterra.crud.transfer;

import java.util.List;

/**
 *
 */
public class Instance {

    public Long id;
    public Template template;
    public Status status;
    public List<Status> availableStatuses;
    public List<Attribute> attributes;
    public List<Link> links;
    public Long version;
}
