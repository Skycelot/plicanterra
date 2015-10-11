package ru.petrosoft.erratum.crud.transfer;

import java.util.List;

/**
 *
 */
public class Link {

    public Long id;
    public String code;
    public String name;
    public String desc;
    public List<Long> values;

    public Link(ru.petrosoft.erratum.metamodel.transfer.Link link) {
        id = link.id;
        code = link.code;
        name = link.name;
        desc = link.desc;
    }
}
