package ru.petrosoft.erratum.metamodel.transfer;

/**
 *
 */
public class Status {

    public Long id;
    public String code;
    public String name;
    public String desc;

    public Status(ru.petrosoft.erratum.metamodel.Status status) {
        id = status.id;
        code = status.code;
        name = status.name;
        desc = status.desc;
    }
}
