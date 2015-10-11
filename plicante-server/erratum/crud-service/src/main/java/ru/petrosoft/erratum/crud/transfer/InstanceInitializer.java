package ru.petrosoft.erratum.crud.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class InstanceInitializer {
    
    public static Instance createInstanceFromTemplate(ru.petrosoft.erratum.metamodel.transfer.Template template) {
        Instance result = null;
        if (template != null) {
            result = new Instance();
            result.template = new Template(template);
            result.attributes = createAttributes(template.attributes.values());
            result.links = createLinks(template.links.values());
            result.status = new Status(template.status);
            result.availableStatuses = createAvailableStatuses(template.availableStatuses.values());
        }
        return result;
    }

    private static List<Attribute> createAttributes(Collection<ru.petrosoft.erratum.metamodel.transfer.Attribute> attributes) {
        List<Attribute> result = new ArrayList<>(attributes.size());
        for (ru.petrosoft.erratum.metamodel.transfer.Attribute attribute: attributes) {
            Attribute resultAttribute = new Attribute(attribute);
            result.add(resultAttribute);
        }
        return result;
    }

    private static List<Link> createLinks(Collection<ru.petrosoft.erratum.metamodel.transfer.Link> links) {
        List<Link> result = new ArrayList<>(links.size());
        for (ru.petrosoft.erratum.metamodel.transfer.Link link: links) {
            Link resultLink = new Link(link);
            result.add(resultLink);
        }
        return result;
    }

    private static List<Status> createAvailableStatuses(Collection<ru.petrosoft.erratum.metamodel.transfer.Status> availableStatuses) {
        List<Status> result = new ArrayList<>(availableStatuses.size());
        for (ru.petrosoft.erratum.metamodel.transfer.Status status: availableStatuses) {
            Status resultStatus = new Status(status);
            result.add(resultStatus);
        }
        return result;
    }
}
