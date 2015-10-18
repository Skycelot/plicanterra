package ru.petrosoft.erratum.crud.transfer;

/**
 *
 */
public class Template {

    public Long id;
    public String code;
    public String name;
    public String desc;

    public Template() {
    }

    Template(ru.petrosoft.erratum.metamodel.transfer.Template template) {
        id = template.id;
        code = template.code;
        name = template.name;
        desc = template.desc;
    }
}
