package ru.petrosoft.erratum.metamodel.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import ru.petrosoft.erratum.metamodel.Attribute;
import ru.petrosoft.erratum.metamodel.Link;
import ru.petrosoft.erratum.metamodel.Project;
import ru.petrosoft.erratum.metamodel.Role;
import ru.petrosoft.erratum.metamodel.Status;
import ru.petrosoft.erratum.metamodel.Template;
import ru.petrosoft.erratum.metamodel.Transition;
import ru.petrosoft.erratum.security.service.SecurityService;
import ru.petrosoft.newage.propertiesservice.ApplicationPropertiesBean;

/**
 *
 */
@Singleton
@Startup
@Lock(LockType.READ)
@EJB(name = "java:global/erratum/MetamodelService", beanInterface = MetamodelService.class)
public class MetamodelService {

    Project project;
    Map<Long, Template> templates;
    Map<Long, Attribute> attributes;
    Map<Long, Link> links;
    Map<Long, Status> statuses;

    @Resource(lookup = "java:/jdbc/metamodel")
    DataSource metamodel;

    @EJB(lookup = "java:global/erratum/SecurityService")
    SecurityService security;

    @EJB(lookup = "java:global/erratum/ApplicationPropertiesBean")
    ApplicationPropertiesBean properties;

    @PostConstruct
    public void init() {
        try (Connection connection = metamodel.getConnection()) {
            project = Project.loadProject(connection, properties.getApplicationCode());
            templates = Template.loadTemplates(connection, project);
            project.gatherProjectTemplates(templates);
            attributes = Attribute.loadAttributes(connection, project.id, templates);
            Template.gatherTemplatesAttributes(attributes);
            links = Link.loadLinks(connection, project.id, templates);
            Template.gatherTemplatesLinks(links);
            statuses = Status.loadStatuses(connection, project.id, templates);
            Map<Long, Transition> transitions = Transition.loadTransitions(connection, project.id, statuses);
            Status.gatherStatusesOutcomes(transitions);
            Template.gatherTemplatesStatusGraphs(templates, statuses);
            Map<Long, Role> roles = Role.loadRoles(connection, project);
            project.gatherProjectRoles(roles);
            Attribute.loadStatusPermissions(connection, project.id, attributes, statuses);
            Attribute.loadRolePermissions(connection, project.id, attributes, roles);
            Link.loadStatusPermissions(connection, project.id, links, statuses);
            Link.loadRolePermissions(connection, project.id, links, roles);
            Transition.loadRolePermissions(connection, project.id, transitions, roles);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает модель шаблона для заданного статуса с учетом пользовательских
     * прав.
     *
     * @param templateId - идентификатор шаблона.
     * @param statusId - идентификатор статуса.
     * @return - модель шаблона.
     */
    public ru.petrosoft.erratum.metamodel.transfer.Template getTemplate(Long templateId, Long statusId) {
        if (templateId != null) {
            Template model = templates.get(templateId);
            if (model != null) {
                if (statusId == null) {
                    statusId = model.statusGraph.id;
                }
                Status status = statuses.get(statusId);
                if (status != null && templateId.equals(status.template.id)) {
                    ru.petrosoft.erratum.metamodel.transfer.Template result = new ru.petrosoft.erratum.metamodel.transfer.Template(model);
                    Set<String> roles = new HashSet<>(security.getCurrentUserRoles());
                    result.attributes = extractAttributes(model, status, roles);
                    result.links = extractLinks(model, status, roles);
                    result.status = new ru.petrosoft.erratum.metamodel.transfer.Status(status);
                    result.availableStatuses = extractAvailableStatuses(status, roles);
                    return result;
                } else {
                    throw new IllegalArgumentException("Template{id=" + templateId + "} doesn't have status{id=" + statusId + "}");
                }
            } else {
                throw new IllegalArgumentException("There is no Template{id=" + templateId + "}");
            }
        } else {
            throw new IllegalArgumentException("templateId is null!");
        }
    }

    private Map<String, ru.petrosoft.erratum.metamodel.transfer.Attribute> extractAttributes(Template model, Status status, Set<String> roles) {
        Map<String, ru.petrosoft.erratum.metamodel.transfer.Attribute> result = new HashMap<>(model.attributes.size() * 2);
        for (Attribute attribute : model.attributes.values()) {
            Set<String> rolePermissions = new HashSet<>(attribute.visibleFor.keySet());
            rolePermissions.retainAll(roles);
            if (!rolePermissions.isEmpty() && attribute.visibleIn.containsKey(status.code)) {
                ru.petrosoft.erratum.metamodel.transfer.Attribute resultAttribute = new ru.petrosoft.erratum.metamodel.transfer.Attribute(attribute);
                rolePermissions = new HashSet<>(attribute.editableFor.keySet());
                rolePermissions.retainAll(roles);
                if (!rolePermissions.isEmpty() && attribute.editableIn.containsKey(status.code)) {
                    resultAttribute.editable = true;
                } else {
                    resultAttribute.editable = false;
                }
                result.put(resultAttribute.code, resultAttribute);
            }
        }
        return result;
    }

    private Map<String, ru.petrosoft.erratum.metamodel.transfer.Link> extractLinks(Template model, Status status, Set<String> roles) {
        Map<String, ru.petrosoft.erratum.metamodel.transfer.Link> result = new HashMap<>(model.links.size() * 2);
        for (Link link : model.links.values()) {
            Set<String> rolePermissions = new HashSet<>(link.visibleFor.keySet());
            rolePermissions.retainAll(roles);
            if (!rolePermissions.isEmpty()) {
                Map<String, Status> visibleIn = link.templateA.equals(model) ? link.visibleAsAIn : link.visibleAsBIn;
                if (visibleIn.containsKey(status.code)) {
                    ru.petrosoft.erratum.metamodel.transfer.Link resultLink = new ru.petrosoft.erratum.metamodel.transfer.Link(link);
                    rolePermissions = new HashSet<>(link.editableFor.keySet());
                    rolePermissions.retainAll(roles);
                    resultLink.editable = false;
                    if (!rolePermissions.isEmpty()) {
                        Map<String, Status> editableIn = link.templateA.equals(model) ? link.editableAsAIn : link.editableAsBIn;
                        if (editableIn.containsKey(status.code)) {
                            resultLink.editable = true;
                        }
                    }
                    result.put(resultLink.code, resultLink);
                }
            }
        }
        return result;
    }

    private Map<String, ru.petrosoft.erratum.metamodel.transfer.Status> extractAvailableStatuses(Status status, Set<String> roles) {
        Map<String, ru.petrosoft.erratum.metamodel.transfer.Status> result;
        if (status.outcomes != null && !status.outcomes.isEmpty()) {
            result = new HashMap<>(status.outcomes.size() * 2);
            for (Transition transition : status.outcomes.values()) {
                Set<String> rolePermissions = new HashSet<>(transition.allowedFor.keySet());
                rolePermissions.retainAll(roles);
                if (!rolePermissions.isEmpty()) {
                    ru.petrosoft.erratum.metamodel.transfer.Status availableStatus = new ru.petrosoft.erratum.metamodel.transfer.Status(transition.outcome);
                    result.put(availableStatus.code, availableStatus);
                }
            }
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }
}
