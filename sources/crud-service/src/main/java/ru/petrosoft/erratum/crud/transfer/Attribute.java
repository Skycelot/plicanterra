package ru.petrosoft.erratum.crud.transfer;

/**
 *
 */
public class Attribute {

    public Long id;
    public String code;
    public String name;
    public String desc;
    public String type;
    public Object value;

    public Attribute() {
    }

    public Attribute(ru.petrosoft.erratum.metamodel.transfer.Attribute attribute) {
        id = attribute.id;
        code = attribute.code;
        name = attribute.name;
        desc = attribute.desc;
        type = attribute.type.name();
    }
}
