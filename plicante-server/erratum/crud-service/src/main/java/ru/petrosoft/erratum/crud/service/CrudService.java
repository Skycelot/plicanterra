package ru.petrosoft.erratum.crud.service;

import java.io.IOException;
import java.util.Collections;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import ru.petrosoft.erratum.crud.persist.Database;
import ru.petrosoft.erratum.metamodel.service.MetamodelService;
import ru.petrosoft.erratum.crud.transfer.Instance;
import ru.petrosoft.erratum.crud.transfer.InstanceInitializer;
import ru.petrosoft.erratum.crud.transfer.search.SearchFilter;
import ru.petrosoft.erratum.crud.transfer.search.SearchResult;

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
        ru.petrosoft.erratum.metamodel.transfer.Template template = metamodel.getTemplate(templateId, null);
        Instance result = InstanceInitializer.createInstanceFromTemplate(template);
        return result;
    }

    public Long createInstance(Instance instance) {
        Long result = null;

        return result;
    }

    /**
     * Создает в базе новый экземпляр объекта по заданному шаблону в
     * первоначальном статусе и возвращает его.
     *
     * @param templateId - идентификатор шаблона.
     * @return - экземпляр объекта.
     */
    public Instance createInstance(Long templateId) {
        ru.petrosoft.erratum.metamodel.transfer.Template template = metamodel.getTemplate(templateId, null);
        Instance instance = InstanceInitializer.createInstanceFromTemplate(template);
        try (Database database = new Database(data)) {
            instance.id = database.getNextSequenceValue(template.code);
            instance.version = 1L;
            database.insert(instance);
            return instance;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Instance findInstance(Long instanceId) {
        Instance result = null;

        return result;
    }

    public SearchResult searchInstances(SearchFilter filter) {
        SearchResult result = new SearchResult();
        result.number = 0;
        result.instances = Collections.emptyList();
        return result;
    }

    public void updateInstance(Instance instance) {

    }

    public void changeInstanceStatus(Long instanceId, Long statusId) {

    }

    public void removeInstance(Long instanceId) {

    }
}
