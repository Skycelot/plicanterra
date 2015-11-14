package ru.skycelot.plicanterra.metamodel.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import ru.skycelot.metamodel.service.MetamodelCrud;
import ru.skycelot.plicanterra.metamodel.Attribute;
import ru.skycelot.plicanterra.metamodel.ElementPermission;
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
//@Startup
@Lock(LockType.READ)
@EJB(name = "java:global/erratum/MetamodelService", beanInterface = MetamodelService.class)
@PermitAll
public class MetamodelService {

    Project project;
    Map<Long, Template> templates;
    Map<Long, Attribute> attributes;
    Map<Long, Link> links;
    Map<Long, Status> statuses;
    Map<Long, Role> roles;

    @EJB
    MetamodelCrud crud;

    @EJB
    SessionService security;

    @EJB
    ApplicationPropertiesBean properties;

    @PostConstruct
    public void init() {
        String applicationCode = properties.getApplicationCode();
        project = crud.loadProject(applicationCode);
        templates = crud.loadTemplates(project);
        attributes = crud.loadAttributes(project.id, templates);
        links = crud.loadLinks(project.id, templates);
        statuses = crud.loadStatuses(project.id, templates);
        roles = crud.loadProjectRoles(project);
        crud.assembleProject(project, roles, templates, attributes, links, statuses);
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
            Long statusId = templates.get(templateId).statuses.get(statusCode).id;
            return getTemplate(templateId, statusId);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ru.skycelot.plicanterra.metamodel.transfer.Template getTemplate(String templateCode, String statusCode) {
        if (project.templates.containsKey(templateCode)) {
            Template template = project.templates.get(templateCode);
            Long statusId = template.statuses.get(statusCode).id;
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
                    statusId = model.initialStatus.id;
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
            Set<String> rolePermissions = new HashSet<>(attribute.rolePermissions.keySet());
            rolePermissions.retainAll(roles);
            if (!rolePermissions.isEmpty() && attribute.statusPermissions.containsKey(status.code)) {
                ru.skycelot.plicanterra.metamodel.transfer.Attribute resultAttribute = new ru.skycelot.plicanterra.metamodel.transfer.Attribute(attribute);
                for (String rolePermission : rolePermissions) {
                    if (attribute.rolePermissions.get(rolePermission) == ElementPermission.WRITE
                            && attribute.statusPermissions.get(status.code) == ElementPermission.WRITE) {
                        resultAttribute.editable = true;
                        break;
                    }
                    result.put(resultAttribute.code, resultAttribute);
                }
            }
        }
        return result;
    }

    private Map<String, ru.skycelot.plicanterra.metamodel.transfer.Link> extractLinks(Template model, Status status, Set<String> roles) {
        Map<String, ru.skycelot.plicanterra.metamodel.transfer.Link> result = new HashMap<>(model.links.size() * 2);
        for (Link link : model.links.values()) {
            Set<String> rolePermissions = new HashSet<>(link.rolePermissions.keySet());
            rolePermissions.retainAll(roles);
            if (!rolePermissions.isEmpty()) {
                boolean leftSide = link.leftTemplate.equals(model);
                if (leftSide ? link.leftStatusPermissions.containsKey(status.code) : link.rightStatusPermissions.containsKey(status.code)) {
                    ru.skycelot.plicanterra.metamodel.transfer.Link resultLink = new ru.skycelot.plicanterra.metamodel.transfer.Link(link);
                    resultLink.referencedTemplate = leftSide ? link.leftTemplate.id : link.rightTemplate.id;
                    switch (link.type) {
                        case ONE_TO_ONE:
                            resultLink.exclusive = true;
                            resultLink.type = leftSide ? LinkType.HOLDER : LinkType.REFERENCED;
                        case ONE_TO_MANY:
                            resultLink.exclusive = false;
                            resultLink.type = leftSide ? LinkType.REFERENCED : LinkType.HOLDER;
                            break;
                        case MANY_TO_ONE:
                            resultLink.exclusive = false;
                            resultLink.type = leftSide ? LinkType.HOLDER : LinkType.REFERENCED;
                            break;
                        case MANY_TO_MANY:
                            resultLink.exclusive = false;
                            resultLink.type = LinkType.JOIN_TABLE;
                            break;
                    }
                    for (String rolePermission : rolePermissions) {
                        if (link.rolePermissions.get(rolePermission) == ElementPermission.WRITE
                                && leftSide
                                        ? link.leftStatusPermissions.get(status.code) == ElementPermission.WRITE
                                        : link.rightStatusPermissions.get(status.code) == ElementPermission.WRITE) {
                            resultLink.editable = true;
                            break;
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
                Set<String> rolePermissions = new HashSet<>(transition.rolePermissions.keySet());
                rolePermissions.retainAll(roles);
                if (!rolePermissions.isEmpty()) {
                    ru.skycelot.plicanterra.metamodel.transfer.Status availableStatus
                            = new ru.skycelot.plicanterra.metamodel.transfer.Status(transition.destinationStatus);
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
