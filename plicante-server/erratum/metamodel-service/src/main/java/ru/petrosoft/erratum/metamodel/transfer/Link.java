package ru.petrosoft.erratum.metamodel.transfer;

/**
 *
 */
public class Link {

    public Long id;
    public Long referencedTemplate;
    public String code;
    public String name;
    public String desc;
    public LinkType type;
    public Boolean editable;

    public Link(ru.petrosoft.erratum.metamodel.Link link) {
        id = link.id;
        code = link.code;
        name = link.name;
        desc = link.desc;
        referencedTemplate = link.templateB.id;
        type = LinkType.valueOf(link.type.name());
    }
}
