package ru.skycelot.plicanterra.crud.transfer;

import java.util.List;
import ru.skycelot.plicanterra.metamodel.transfer.LinkType;

/**
 *
 */
public class Link {

    public Long id;
    public String code;
    public String name;
    public String desc;
    public Boolean multipleValues;
    public List<Long> values;

    public Link() {
    }

    public Link(ru.skycelot.plicanterra.metamodel.transfer.Link link) {
        id = link.id;
        code = link.code;
        name = link.name;
        desc = link.desc;
        multipleValues = link.type == LinkType.JOIN_TABLE || (link.type == LinkType.REFERENCED && link.exclusive == false);
    }
}
