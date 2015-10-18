package ru.skycelot.plicanterra.metamodel.transfer;

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
    public Boolean exclusive;
    public Boolean editable;

    public Link(ru.skycelot.plicanterra.metamodel.Link link) {
        id = link.id;
        code = link.code;
        name = link.name;
        desc = link.desc;
    }
}
