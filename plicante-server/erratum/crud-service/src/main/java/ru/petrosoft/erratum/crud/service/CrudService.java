package ru.petrosoft.erratum.crud.service;

import java.io.IOException;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import ru.petrosoft.erratum.crud.persist.Database;
import ru.petrosoft.erratum.crud.persist.InsertQuery;
import ru.petrosoft.erratum.crud.transfer.Attribute;
import ru.petrosoft.erratum.metamodel.service.MetamodelService;
import ru.petrosoft.erratum.crud.transfer.Instance;
import ru.petrosoft.erratum.crud.transfer.InstanceInitializer;
import ru.petrosoft.erratum.crud.transfer.Link;
import ru.petrosoft.erratum.crud.transfer.search.SearchFilter;
import ru.petrosoft.erratum.crud.transfer.search.SearchResult;
import ru.petrosoft.erratum.metamodel.transfer.AttributeType;
import ru.petrosoft.erratum.metamodel.transfer.LinkType;
import ru.petrosoft.erratum.util.Argument;
import ru.petrosoft.erratum.util.ArgumentsChecker;

/**
 *
 */
@Stateless
@EJB(name = "java:global/erratum/CrudService", beanInterface = CrudService.class)
@PermitAll
public class CrudService {

    @EJB(lookup = "java:global/erratum/MetamodelService")
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
        ru.petrosoft.erratum.metamodel.transfer.Template template = metamodel.getTemplate(templateId);
        Instance result = InstanceInitializer.createInstanceFromTemplate(template);
        return result;
    }

    public Long createInstance(Instance instance) {
        ArgumentsChecker.notNull(new Argument("instance", instance));
        ArgumentsChecker.notNull(new Argument("instance.template", instance.template));
        try (Database database = new Database(data)) {
            ru.petrosoft.erratum.metamodel.transfer.Template template = null;
            if (instance.template.id != null) {
                template = metamodel.getTemplate(instance.template.id);
            } else if (instance.template.code != null) {
                template = metamodel.getTemplate(instance.template.code);
            }
            if (template != null) {
                if (template.status.id.equals(instance.status.id) || template.status.code.equals(instance.status.code)) {
                    instance.id = database.getNextInstanceSequenceValue();
                    database.saveTemplateCodeOfInstance(instance.id, instance.template.code);
                    instance.version = 1L;
                    InsertQuery query = new InsertQuery(instance.template.code);
                    query.addInlineColumn("ID", instance.id);
                    query.addInlineColumn("STATUS_ID", template.status.id);
                    query.addInlineColumn("VERSION", instance.version);
                    if (instance.attributes != null) {
                        for (Attribute attribute : instance.attributes) {
                            if (attribute != null) {
                                ru.petrosoft.erratum.metamodel.transfer.Attribute metaAttribute = null;
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
                                }
                            }
                        }
                    }
                    if (instance.links != null) {
                        for (Link link : instance.links) {
                            if (link != null) {
                                ru.petrosoft.erratum.metamodel.transfer.Link metaLink = null;
                                if (link.code != null) {
                                    metaLink = template.links.get(link.code);
                                } else if (link.id != null) {
                                    metaLink = template.links.get(metamodel.getLinkCodeById(link.id));
                                }
                                if (metaLink != null) {
                                    if (metaLink.type == LinkType.HOLDER) {
                                        if (metaLink.exclusive) {
                                            //check use
                                        }
                                        if (link.values != null && link.values.size() == 1 && link.values.get(0) != null) {
                                            query.addInlineColumn(link.code, link.values.get(0));
                                        }
                                    } else {
                                        //insert link
                                    }
                                }
                            }
                        }
                    }
                    database.insert(query);
                    //insert links
                    return instance.id;
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException();
            }
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
        ru.petrosoft.erratum.metamodel.transfer.Template template = metamodel.getTemplate(templateId);
        Instance instance = InstanceInitializer.createInstanceFromTemplate(template);
        try (Database database = new Database(data)) {
            instance.id = database.getNextInstanceSequenceValue();
            database.saveTemplateCodeOfInstance(instance.id, instance.template.code);
            instance.version = 1L;
            InsertQuery query = new InsertQuery(instance.template.code);
            query.addInlineColumn("ID", instance.id);
            query.addInlineColumn("STATUS_ID", instance.status.id);
            query.addInlineColumn("VERSION", instance.version);
            database.insert(query);
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
}
