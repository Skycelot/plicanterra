package ru.petrosoft.erratum.metamodel.transfer;

/**
 *
 */
public class Attribute {

    public Long id;
    public String code;
    public String name;
    public String desc;
    public AttributeType type;
    public Boolean editable;

    public Attribute(ru.petrosoft.erratum.metamodel.Attribute attribute) {
        id = attribute.id;
        code = attribute.code;
        name = attribute.name;
        desc = attribute.desc;
        type = AttributeType.valueOf(attribute.type.name());
    }
}
