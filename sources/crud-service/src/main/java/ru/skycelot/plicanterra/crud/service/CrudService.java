package ru.skycelot.plicanterra.crud.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import ru.skycelot.plicanterra.crud.persist.Database;
import ru.skycelot.plicanterra.crud.persist.InsertQuery;
import ru.skycelot.plicanterra.crud.persist.UpdateQuery;
import ru.skycelot.plicanterra.crud.transfer.Attribute;
import ru.skycelot.plicanterra.metamodel.service.MetamodelService;
import ru.skycelot.plicanterra.crud.transfer.Instance;
import ru.skycelot.plicanterra.crud.transfer.InstanceInitializer;
import ru.skycelot.plicanterra.crud.transfer.Link;
import ru.skycelot.plicanterra.crud.transfer.Status;
import ru.skycelot.plicanterra.crud.transfer.Template;
import ru.skycelot.plicanterra.crud.transfer.search.SearchFilter;
import ru.skycelot.plicanterra.crud.transfer.search.SearchResult;
import ru.skycelot.plicanterra.metamodel.transfer.AttributeType;
import ru.skycelot.plicanterra.util.Argument;
import ru.skycelot.plicanterra.util.ArgumentsChecker;

/**
 *
 */
@Stateless
@EJB(name = "java:global/erratum/CrudService", beanInterface = CrudService.class)
@PermitAll
public class CrudService {

    @EJB
    MetamodelService metamodel;

    @Resource(lookup = "java:/jdbc/data")
    DataSource data;

    /**
     * Возвращает экземпляр объекта по заданному шаблону в первоначальном
     * статусе без занесения в базу данных.
     *
     * @param templateId - идентификатор шаблона.
     * @return - экземпляр объекта.
     */
    public Instance getSketchInstance(Long templateId) {
        ru.skycelot.plicanterra.metamodel.transfer.Template template = metamodel.getTemplate(templateId);
        Instance result = InstanceInitializer.createInstanceFromTemplate(template);
        return result;
    }

    /**
     * Вставляет в базу заполненный клиентом объект в начальном статусе.
     *
     * @param instance - заполненный экземпляр объекта
     * @return - идентификатор созданного объекта.
     */
    public Long createInstance(Instance instance) {
        ArgumentsChecker.notNull(new Argument("instance", instance));
        try (Database database = new Database(data)) {
            ru.skycelot.plicanterra.metamodel.transfer.Template template = findTemplate(instance.template);
            checkStatusForSameness(instance.status, template.status, template.code);
            instance.id = database.getNextInstanceSequenceValue();
            database.saveTemplateCodeOfInstance(instance.id, instance.template.code);
            instance.version = 1L;
            InsertQuery query = new InsertQuery(instance.template.code);
            query.addInlineColumn("ID", instance.id);
            query.addInlineColumn("STATUS_ID", template.status.id);
            query.addInlineColumn("VERSION", instance.version);
            processAttributes(instance.attributes, template, query);
            List<InsertQuery> joinLinks = new LinkedList<>();
            List<UpdateQuery> referencedLinks = new LinkedList<>();
            processLinks(instance.links, template, query, joinLinks, referencedLinks, instance.id);
            database.execute(query);
            for (InsertQuery linkQuery : joinLinks) {
                database.execute(linkQuery);
            }
            for (UpdateQuery linkQuery : referencedLinks) {
                database.execute(linkQuery);
            }
            return instance.id;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создает в базе новый экземпляр объекта по заданному шаблону в
     * первоначальном статусе и возвращает его.
     *
     * @param templateId - идентификатор шаблона.
     * @return - экземпляр объекта.
     */
    public Instance createInstance(Long templateId) {
        ru.skycelot.plicanterra.metamodel.transfer.Template template = metamodel.getTemplate(templateId);
        Instance instance = InstanceInitializer.createInstanceFromTemplate(template);
        try (Database database = new Database(data)) {
            instance.id = database.getNextInstanceSequenceValue();
            database.saveTemplateCodeOfInstance(instance.id, instance.template.code);
            instance.version = 1L;
            InsertQuery query = new InsertQuery(instance.template.code);
            query.addInlineColumn("ID", instance.id);
            query.addInlineColumn("STATUS_ID", instance.status.id);
            query.addInlineColumn("VERSION", instance.version);
            database.execute(query);
            return instance;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Instance findInstance(Long instanceId) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public SearchResult searchInstances(SearchFilter filter) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public void updateInstance(Instance instance) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public void changeInstanceStatus(Long instanceId, Long statusId) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public void removeInstance(Long instanceId) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    private ru.skycelot.plicanterra.metamodel.transfer.Template findTemplate(Template template) {
        ArgumentsChecker.notNull(new Argument("template", template));
        ru.skycelot.plicanterra.metamodel.transfer.Template result;
        if (template.id != null) {
            result = metamodel.getTemplate(template.id);
        } else if (template.code != null) {
            result = metamodel.getTemplate(template.code);
        } else {
            throw new IllegalArgumentException("Template id or code is not specified!");
        }
        return result;
    }

    private void checkStatusForSameness(Status sourceStatus, ru.skycelot.plicanterra.metamodel.transfer.Status metaStatus, String templateCode) {
        if (sourceStatus != null) {
            if (sourceStatus.code != null) {
                if (!sourceStatus.code.equals(metaStatus.code)) {
                    throw new IllegalArgumentException("Status{code=" + sourceStatus.code + "} isn't initial status of Template{code=" + templateCode + "}");
                }
            }
            if (sourceStatus.id != null) {
                if (!sourceStatus.id.equals(metaStatus.id)) {
                    throw new IllegalArgumentException("Status{id=" + sourceStatus.id + "} isn't initial status of Template{code=" + templateCode + "}");
                }
            }
        }
    }

    private void processAttributes(List<Attribute> attributes, ru.skycelot.plicanterra.metamodel.transfer.Template template, InsertQuery query) {
        if (attributes != null) {
            for (Attribute attribute : attributes) {
                if (attribute != null) {
                    ru.skycelot.plicanterra.metamodel.transfer.Attribute metaAttribute = null;
                    if (attribute.code != null) {
                        metaAttribute = template.attributes.get(attribute.code);
                    } else if (attribute.id != null) {
                        metaAttribute = template.attributes.get(metamodel.getAttributeCodeById(attribute.id));
                    }
                    if (metaAttribute != null) {
                        if (metaAttribute.type == AttributeType.STRING && (attribute.value == null) || attribute.value instanceof String) {
                            query.addParameterColumn(metaAttribute.code, (String) attribute.value);
                        } else {
                            query.addInlineColumn(metaAttribute.code, attribute.value);
                        }
                    } else {
                        throw new IllegalArgumentException("Template{id=" + template.id + ", code=" + template.code + "} doesn't have Attribute{id=" + attribute.id + ", code=" + attribute.code + "}");
                    }
                }
            }
        }
    }

    private void processLinks(List<Link> links, ru.skycelot.plicanterra.metamodel.transfer.Template template,
            InsertQuery query, List<InsertQuery> joinLinks, List<UpdateQuery> referencedLinks, Long instanceId) {
        if (links != null) {
            for (Link link : links) {
                if (link != null) {
                    ru.skycelot.plicanterra.metamodel.transfer.Link metaLink = null;
                    if (link.code != null) {
                        metaLink = template.links.get(link.code);
                    } else if (link.id != null) {
                        metaLink = template.links.get(metamodel.getLinkCodeById(link.id));
                    }
                    if (metaLink != null) {
                        if (link.values != null && link.values.size() == 1 && link.values.get(0) != null) {
                            switch (metaLink.type) {
                                case HOLDER:
                                    if (metaLink.exclusive) {
                                        //check use
                                    }
                                    query.addInlineColumn(link.code, link.values.get(0));
                                    break;
                                case JOIN_TABLE:
                                    InsertQuery joinQuery = new InsertQuery(link.code);
                                    joinQuery.addInlineColumn(metaLink.code, instanceId);
                                    joinQuery.addInlineColumn(metamodel.getTemplate(metaLink.referencedTemplate).code, link.values.get(0));
                                    joinLinks.add(joinQuery);
                                    break;
                                case REFERENCED:
                                    if (metaLink.exclusive) {
                                        //check use
                                    }
                                    UpdateQuery referencedQuery = new UpdateQuery(metamodel.getTemplate(metaLink.referencedTemplate).code, link.values.get(0));
                                    referencedQuery.addInlineColumn(metaLink.code, instanceId);
                                    break;
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("Template{id=" + template.id + ", code=" + template.code + "} doesn't have Link{id=" + link.id + ", code=" + link.code + "}");
                    }
                }
            }
        }
    }
}
