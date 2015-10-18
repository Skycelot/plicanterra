package ru.skycelot.plicanterra.crud.transfer;

/**
 *
 */
public class Status {

    public Long id;
    public String code;
    public String name;
    public String desc;

    public Status() {
    }

    public Status(ru.skycelot.plicanterra.metamodel.transfer.Status status) {
        id = status.id;
        code = status.code;
        name = status.name;
        desc = status.desc;
    }
}
