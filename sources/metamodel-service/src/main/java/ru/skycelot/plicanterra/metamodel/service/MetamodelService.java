package ru.skycelot.plicanterra.metamodel.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import ru.skycelot.plicanterra.metamodel.Attribute;
import ru.skycelot.plicanterra.metamodel.Link;
import ru.skycelot.plicanterra.metamodel.Project;
import ru.skycelot.plicanterra.metamodel.Role;
import ru.skycelot.plicanterra.metamodel.Status;
import ru.skycelot.plicanterra.metamodel.Template;
import ru.skycelot.plicanterra.metamodel.Transition;
import ru.skycelot.plicanterra.metamodel.transfer.LinkType;
import ru.skycelot.plicanterra.security.service.SessionService;
import ru.skycelot.plicanterra.util.Argument;
import ru.skycelot.plicanterra.util.ArgumentsChecker;
import ru.skycelot.plicanterra.properties.ApplicationPropertiesBean;

/**
 *
 */
@Singleton
@Startup
@Lock(LockType.READ)
@EJB(name = "java:global/erratum/MetamodelService", beanInterface = MetamodelService.class)
@PermitAll
public class MetamodelService {

    Project project;
    Map<Long, Template> templates;
    Map<Long, Attribute> attributes;
    Map<Long, Link> links;
    Map<Long, Status> statuses;

    @Resource(lookup = "java:/jdbc/metamodel")
    DataSource metamodel;

    @EJB
    SessionService security;

    @EJB
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

    public ru.skycelot.plicanterra.metamodel.transfer.Template getTemplate(String templateCode, Long statusId) {
        if (project.templates.containsKey(templateCode)) {
            Long templateId = project.templates.get(templateCode).id;
            return getTemplate(templateId, statusId);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ru.skycelot.plicanterra.metamodel.transfer.Template getTemplate(Long templateId) {
        return getTemplate(templateId, (Long) null);
    }

    public ru.skycelot.plicanterra.metamodel.transfer.Template getTemplate(String templateCode) {
        if (project.templates.containsKey(templateCode)) {
            Long templateId = project.templates.get(templateCode).id;
            return getTemplate(templateId, (Long) null);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ru.skycelot.plicanterra.metamodel.transfer.Template getTemplate(Long templateId, String statusCode) {
        if (templates.containsKey(templateId)) {
            Long statusId = templates.get(templateId).findStatus(statusCode).id;
            return getTemplate(templateId, statusId);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ru.skycelot.plicanterra.metamodel.transfer.Template getTemplate(String templateCode, String statusCode) {
        if (project.templates.containsKey(templateCode)) {
            Template template = project.templates.get(templateCode);
            Long statusId = template.findStatus(statusCode).id;
            return getTemplate(template.id, statusId);
        } else {
            throw new IllegalArgumentException();
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
    public ru.skycelot.plicanterra.metamodel.transfer.Template getTemplate(Long templateId, Long statusId) {
        if (templateId != null) {
            Template model = templates.get(templateId);
            if (model != null) {
                if (statusId == null) {
                    statusId = model.statusGraph.id;
                }
                Status status = statuses.get(statusId);
                if (status != null && templateId.equals(status.template.id)) {
                    ru.skycelot.plicanterra.metamodel.transfer.Template result = new ru.skycelot.plicanterra.metamodel.transfer.Template(model);
                    Set<String> roles = new HashSet<>(security.getCurrentPrincipalRoles());
                    result.attributes = extractAttributes(model, status, roles);
                    result.links = extractLinks(model, status, roles);
                    result.status = new ru.skycelot.plicanterra.metamodel.transfer.Status(status);
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

    private Map<String, ru.skycelot.plicanterra.metamodel.transfer.Attribute> extractAttributes(Template model, Status status, Set<String> roles) {
        Map<String, ru.skycelot.plicanterra.metamodel.transfer.Attribute> result = new HashMap<>(model.attributes.size() * 2);
        for (Attribute attribute : model.attributes.values()) {
            Set<String> rolePermissions = new HashSet<>(attribute.visibleFor.keySet());
            rolePermissions.retainAll(roles);
            if (!rolePermissions.isEmpty() && attribute.visibleIn.containsKey(status.code)) {
                ru.skycelot.plicanterra.metamodel.transfer.Attribute resultAttribute = new ru.skycelot.plicanterra.metamodel.transfer.Attribute(attribute);
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

    private Map<String, ru.skycelot.plicanterra.metamodel.transfer.Link> extractLinks(Template model, Status status, Set<String> roles) {
        Map<String, ru.skycelot.plicanterra.metamodel.transfer.Link> result = new HashMap<>(model.links.size() * 2);
        for (Link link : model.links.values()) {
            Set<String> rolePermissions = new HashSet<>(link.visibleFor.keySet());
            rolePermissions.retainAll(roles);
            if (!rolePermissions.isEmpty()) {
                Map<String, Status> visibleIn = link.templateA.equals(model) ? link.visibleAsAIn : link.visibleAsBIn;
                if (visibleIn.containsKey(status.code)) {
                    ru.skycelot.plicanterra.metamodel.transfer.Link resultLink = new ru.skycelot.plicanterra.metamodel.transfer.Link(link);
                    resultLink.referencedTemplate = link.templateA.equals(model) ? link.templateA.id : link.templateB.id;
                    switch (link.type) {
                        case ONE_TO_ONE:
                            resultLink.exclusive = true;
                            resultLink.type = link.templateA.equals(model) ? LinkType.HOLDER : LinkType.REFERENCED;
                        case ONE_TO_MANY:
                            resultLink.exclusive = false;
                            resultLink.type = link.templateA.equals(model) ? LinkType.REFERENCED : LinkType.HOLDER;
                            break;
                        case MANY_TO_ONE:
                            resultLink.exclusive = false;
                            resultLink.type = link.templateA.equals(model) ? LinkType.HOLDER : LinkType.REFERENCED;
                            break;
                        case MANY_TO_MANY:
                            resultLink.exclusive = false;
                            resultLink.type = LinkType.JOIN_TABLE;
                            break;
                    }
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

    private Map<String, ru.skycelot.plicanterra.metamodel.transfer.Status> extractAvailableStatuses(Status status, Set<String> roles) {
        Map<String, ru.skycelot.plicanterra.metamodel.transfer.Status> result;
        if (status.outcomes != null && !status.outcomes.isEmpty()) {
            result = new HashMap<>(status.outcomes.size() * 2);
            for (Transition transition : status.outcomes.values()) {
                Set<String> rolePermissions = new HashSet<>(transition.allowedFor.keySet());
                rolePermissions.retainAll(roles);
                if (!rolePermissions.isEmpty()) {
                    ru.skycelot.plicanterra.metamodel.transfer.Status availableStatus = new ru.skycelot.plicanterra.metamodel.transfer.Status(transition.outcome);
                    result.put(availableStatus.code, availableStatus);
                }
            }
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    public String getAttributeCodeById(Long attributeId) {
        ArgumentsChecker.notNull(new Argument("attributeId", attributeId));
        return attributes.get(attributeId).code;
    }

    public String getLinkCodeById(Long linkId) {
        ArgumentsChecker.notNull(new Argument("linkId", linkId));
        return links.get(linkId).code;
    }
}
