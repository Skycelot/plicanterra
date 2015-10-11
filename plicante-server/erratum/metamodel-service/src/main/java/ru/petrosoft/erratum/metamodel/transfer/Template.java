package ru.petrosoft.erratum.metamodel.transfer;

import java.util.Map;

/**
 *
 */
public class Template {

    public Long id;
    public String code;
    public String name;
    public String desc;
    public Map<String, Attribute> attributes;
    public Map<String, Link> links;
    public Status status;
    public Map<String, Status> availableStatuses;

    public Template() {
    }

    public Template(ru.petrosoft.erratum.metamodel.Template model) {
        this.id = model.id;
        this.code = model.code;
        this.name = model.name;
        this.desc = model.desc;
    }
}
