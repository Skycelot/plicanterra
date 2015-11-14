package ru.skycelot.metamodel.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import ru.skycelot.metamodel.persistence.AttributeGrants;
import ru.skycelot.metamodel.persistence.AttributeGrants_;
import ru.skycelot.metamodel.persistence.AttributePermissions;
import ru.skycelot.metamodel.persistence.AttributePermissions_;
import ru.skycelot.metamodel.persistence.Attribute_;
import ru.skycelot.metamodel.persistence.LinkGrants;
import ru.skycelot.metamodel.persistence.LinkGrants_;
import ru.skycelot.metamodel.persistence.LinkPermissions;
import ru.skycelot.metamodel.persistence.LinkPermissions_;
import ru.skycelot.metamodel.persistence.Link_;
import ru.skycelot.metamodel.persistence.Profile_;
import ru.skycelot.metamodel.persistence.Project_;
import ru.skycelot.metamodel.persistence.Role_;
import ru.skycelot.metamodel.persistence.Status_;
import ru.skycelot.metamodel.persistence.TemplatePermissions;
import ru.skycelot.metamodel.persistence.TemplatePermissions_;
import ru.skycelot.metamodel.persistence.Template_;
import ru.skycelot.metamodel.persistence.Transition_;
import ru.skycelot.metamodel.persistence.User_;
import ru.skycelot.plicanterra.metamodel.Attribute;
import ru.skycelot.plicanterra.metamodel.ElementPermission;
import ru.skycelot.plicanterra.metamodel.Link;
import ru.skycelot.plicanterra.metamodel.Profile;
import ru.skycelot.plicanterra.metamodel.Project;
import ru.skycelot.plicanterra.metamodel.Role;
import ru.skycelot.plicanterra.metamodel.Status;
import ru.skycelot.plicanterra.metamodel.Template;
import ru.skycelot.plicanterra.metamodel.TemplatePermission;
import ru.skycelot.plicanterra.metamodel.Transition;
import ru.skycelot.plicanterra.metamodel.User;
import ru.skycelot.plicanterra.util.Argument;
import ru.skycelot.plicanterra.util.ArgumentsChecker;

/**
 *
 */
@Stateless
public class MetamodelCrud {

    @PersistenceContext
    EntityManager em;

    public Map<Long, Role> loadProjectRoles(Project project) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<ru.skycelot.metamodel.persistence.Role> roleRoot = queryBody.from(ru.skycelot.metamodel.persistence.Role.class);
        Path<Long> roleIdElement = roleRoot.get(Role_.id);
        Path<String> roleCodeElement = roleRoot.get(Role_.code);
        Path<String> roleNameElement = roleRoot.get(Role_.name);
        Path<String> roleDescriptionElement = roleRoot.get(Role_.description);
        queryBody.select(cb.tuple(roleIdElement, roleCodeElement, roleNameElement, roleDescriptionElement));
        queryBody.where(cb.equal(roleRoot.get(Role_.project).get(Project_.id), project.id));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        Map<Long, Role> result;
        if (!queryResult.isEmpty()) {
            result = new HashMap<>(queryResult.size() * 2);
            for (Tuple row : queryResult) {
                Role role = new Role();
                role.id = row.get(roleIdElement);
                role.code = row.get(roleCodeElement);
                role.name = row.get(roleNameElement);
                role.description = row.get(roleDescriptionElement);
                role.project = project;
                result.put(role.id, role);
                return result;
            }
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    public Map<Long, Template> loadTemplates(Project project) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<ru.skycelot.metamodel.persistence.Template> templateRoot = queryBody.from(ru.skycelot.metamodel.persistence.Template.class);
        Path<Long> templateIdElement = templateRoot.get(Template_.id);
        Path<String> templateCodeElement = templateRoot.get(Template_.code);
        Path<String> templateNameElement = templateRoot.get(Template_.name);
        Path<String> templateDescriptionElement = templateRoot.get(Template_.description);
        queryBody.select(cb.tuple(templateIdElement, templateCodeElement, templateNameElement, templateDescriptionElement));
        queryBody.where(cb.equal(templateRoot.get(Template_.project).get(Project_.id), project.id));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        Map<Long, Template> result;
        if (!queryResult.isEmpty()) {
            result = new HashMap<>(queryResult.size() * 2);
            for (Tuple row : queryResult) {
                Template template = new Template();
                template.id = row.get(templateIdElement);
                template.code = row.get(templateCodeElement);
                template.name = row.get(templateNameElement);
                template.description = row.get(templateDescriptionElement);
                template.project = project;
                result.put(template.id, template);
            }
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    public Map<Long, Attribute> loadAttributes(Long projectId, Map<Long, Template> templates) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<ru.skycelot.metamodel.persistence.Attribute> attributeRoot = queryBody.from(ru.skycelot.metamodel.persistence.Attribute.class);
        Path<Long> attributeIdElement = attributeRoot.get(Attribute_.id);
        Path<String> attributeCodeElement = attributeRoot.get(Attribute_.code);
        Path<String> attributeNameElement = attributeRoot.get(Attribute_.name);
        Path<String> attributeDescriptionElement = attributeRoot.get(Attribute_.description);
        Path<Long> attributeTemplateIdElement = attributeRoot.get(Attribute_.template).get(Template_.id);
        queryBody.select(cb.tuple(attributeIdElement, attributeCodeElement, attributeNameElement,
                attributeDescriptionElement, attributeTemplateIdElement));
        queryBody.where(cb.equal(attributeRoot.get(Attribute_.template).get(Template_.project).get(Project_.id), projectId));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        Map<Long, Attribute> result;
        if (!queryResult.isEmpty()) {
            result = new HashMap<>(queryResult.size() * 2);
            for (Tuple row : queryResult) {
                Attribute attribute = new Attribute();
                attribute.id = row.get(attributeIdElement);
                attribute.code = row.get(attributeCodeElement);
                attribute.name = row.get(attributeNameElement);
                attribute.description = row.get(attributeDescriptionElement);
                Long templateId = row.get(attributeTemplateIdElement);
                if (!templates.containsKey(templateId)) {
                    throw new IllegalStateException("Template{id=" + templateId + "} is not loaded for Attribute{id=" + attribute.id + "}!");
                }
                Template template = templates.get(templateId);
                attribute.template = template;
                result.put(attribute.id, attribute);
            }
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    public Map<Long, Link> loadLinks(Long projectId, Map<Long, Template> templates) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<ru.skycelot.metamodel.persistence.Link> linkRoot = queryBody.from(ru.skycelot.metamodel.persistence.Link.class);
        Path<Long> linkIdElement = linkRoot.get(Link_.id);
        Path<String> linkCodeElement = linkRoot.get(Link_.code);
        Path<String> linkNameElement = linkRoot.get(Link_.name);
        Path<String> linkDescriptionElement = linkRoot.get(Link_.description);
        Path<Long> linkLeftTemplateIdElement = linkRoot.get(Link_.leftTemplate).get(Template_.id);
        Path<Long> linkRightTemplateIdElement = linkRoot.get(Link_.rightTemplate).get(Template_.id);
        queryBody.select(
                cb.tuple(
                        linkIdElement, linkCodeElement, linkNameElement,
                        linkDescriptionElement, linkLeftTemplateIdElement, linkRightTemplateIdElement));
        queryBody.where(
                cb.or(
                        cb.equal(linkRoot.get(Link_.leftTemplate).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(linkRoot.get(Link_.rightTemplate).get(Template_.project).get(Project_.id), projectId)));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        Map<Long, Link> result;
        if (!queryResult.isEmpty()) {
            result = new HashMap<>(queryResult.size() * 2);
            for (Tuple row : queryResult) {
                Link link = new Link();
                link.id = row.get(linkIdElement);
                link.code = row.get(linkCodeElement);
                link.name = row.get(linkNameElement);
                link.description = row.get(linkDescriptionElement);
                Long leftTemplateId = row.get(linkLeftTemplateIdElement);
                if (!templates.containsKey(leftTemplateId)) {
                    throw new IllegalStateException("Template{id=" + leftTemplateId + "} is not loaded for Link{id=" + link.id + "}!");
                }
                Template leftTemplate = templates.get(leftTemplateId);
                link.leftTemplate = leftTemplate;
                Long rightTemplateId = row.get(linkRightTemplateIdElement);
                if (!templates.containsKey(rightTemplateId)) {
                    throw new IllegalStateException("Template{id=" + rightTemplateId + "} is not loaded for Link{id=" + link.id + "}!");
                }
                Template rightTemplate = templates.get(rightTemplateId);
                link.rightTemplate = rightTemplate;
                result.put(link.id, link);
            }
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    public Map<Long, Status> loadStatuses(Long projectId, Map<Long, Template> templates) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<ru.skycelot.metamodel.persistence.Status> statusRoot = queryBody.from(ru.skycelot.metamodel.persistence.Status.class);
        Path<Long> statusIdElement = statusRoot.get(Status_.id);
        Path<String> statusCodeElement = statusRoot.get(Status_.code);
        Path<String> statusNameElement = statusRoot.get(Status_.name);
        Path<String> statusDescriptionElement = statusRoot.get(Status_.description);
        Path<Long> statusTemplateIdElement = statusRoot.get(Status_.template).get(Template_.id);
        queryBody.select(cb.tuple(statusIdElement, statusCodeElement, statusNameElement,
                statusDescriptionElement, statusTemplateIdElement));
        queryBody.where(cb.equal(statusRoot.get(Status_.template).get(Template_.project).get(Project_.id), projectId));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        Map<Long, Status> result;
        if (!queryResult.isEmpty()) {
            result = new HashMap<>(queryResult.size() * 2);
            for (Tuple row : queryResult) {
                Status status = new Status();
                status.id = row.get(statusIdElement);
                status.code = row.get(statusCodeElement);
                status.name = row.get(statusNameElement);
                status.description = row.get(statusDescriptionElement);
                Long templateId = row.get(statusTemplateIdElement);
                if (!templates.containsKey(templateId)) {
                    throw new IllegalStateException("Template{id=" + templateId + "} is not loaded for Status{id=" + status.id + "}!");
                }
                Template template = templates.get(templateId);
                status.template = template;
                result.put(status.id, status);
            }
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    public Map<Long, Transition> loadTransitions(Long projectId, Map<Long, Status> statuses) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<ru.skycelot.metamodel.persistence.Transition> transitionRoot = queryBody.from(ru.skycelot.metamodel.persistence.Transition.class);
        Path<Long> transitionIdElement = transitionRoot.get(Transition_.id);
        Path<Long> sourceStatusIdElement = transitionRoot.get(Transition_.sourceStatus).get(Status_.id);
        Path<Long> destinationStatusIdElement = transitionRoot.get(Transition_.destinationStatus).get(Status_.id);
        queryBody.select(cb.tuple(transitionIdElement, sourceStatusIdElement, destinationStatusIdElement));
        queryBody.where(
                cb.or(
                        cb.equal(transitionRoot.get(Transition_.sourceStatus).get(Status_.template).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(transitionRoot.get(Transition_.destinationStatus).get(Status_.template).get(Template_.project).get(Project_.id), projectId)));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        Map<Long, Transition> result;
        if (!queryResult.isEmpty()) {
            result = new HashMap<>(queryResult.size() * 2);
            for (Tuple row : queryResult) {
                Transition transition = new Transition();
                transition.id = row.get(transitionIdElement);
                Long sourceStatusId = row.get(sourceStatusIdElement);
                if (!statuses.containsKey(sourceStatusId)) {
                    throw new IllegalStateException("Status{id=" + sourceStatusId + "} is not loaded for Transition{id=" + transition.id + "}!");
                }
                Status sourceStatus = statuses.get(sourceStatusId);
                transition.sourceStatus = sourceStatus;
                Long destinationStatusId = row.get(destinationStatusIdElement);
                if (!statuses.containsKey(destinationStatusId)) {
                    throw new IllegalStateException("Status{id=" + destinationStatusId + "} is not loaded for Transition{id=" + transition.id + "}!");
                }
                Status destinationStatus = statuses.get(destinationStatusId);
                transition.destinationStatus = destinationStatus;
                result.put(transition.id, transition);
            }
        } else {
            result = Collections.emptyMap();
        }
        return result;
    }

    public Project loadProject(String projectCode) {
        ArgumentsChecker.notNull(new Argument("projectCode", projectCode));
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<ru.skycelot.metamodel.persistence.Project> projectRoot = queryBody.from(ru.skycelot.metamodel.persistence.Project.class);
        Path<Long> projectIdElement = projectRoot.get(Project_.id);
        Path<String> projectNameElement = projectRoot.get(Project_.name);
        Path<String> projectDescriptionElement = projectRoot.get(Project_.description);
        queryBody.select(cb.tuple(projectIdElement, projectNameElement, projectDescriptionElement));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        switch (queryResult.size()) {
            case 0:
                throw new IllegalStateException("There is no Project{code=" + projectCode + "}!");
            case 1:
                Tuple row = queryResult.get(0);
                Project result = new Project();
                result.id = row.get(projectIdElement);
                result.code = projectCode;
                result.name = row.get(projectNameElement);
                result.description = row.get(projectDescriptionElement);
                return result;
            default:
                throw new IllegalStateException("The project code {" + projectCode + "} is not unique!");
        }
    }

    public void assembleProject(Project project, Map<Long, Role> roles, Map<Long, Template> templates,
            Map<Long, Attribute> attributes, Map<Long, Link> links, Map<Long, Status> statuses) {
        project.roles = roles.values().stream().collect(Collectors.toMap(role -> role.code, role -> role));
        project.templates = templates.values().stream().collect(Collectors.toMap(template -> template.code, template -> template));
        loadTemplatePermissions(project.id, templates, roles);
        assembleAttributesToTemplates(attributes);
        assembleLinksToTemplates(links);
        assembleStatusesToTemplates(statuses);
        Map<Long, Transition> transitions = loadTransitions(project.id, statuses);
        assembleTransitionsToStatuses(transitions);
        loadAttributeGrants(project.id, attributes, statuses);
        loadLinkGrants(project.id, links, statuses);
        loadAttributePermissions(project.id, attributes, roles);
        loadLinkPermissions(project.id, links, roles);
        loadTransitionPermissions(project.id, transitions, roles);
    }

    private void assembleRolesToProject(Project project, Map<Long, Role> roles) {
    }

    private void assembleTemplatesToProject(Project project, Map<Long, Template> templates) {
    }

    private void loadTemplatePermissions(Long projectId, Map<Long, Template> templates, Map<Long, Role> roles) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<TemplatePermissions> permissionsRoot = queryBody.from(TemplatePermissions.class);
        Path<Long> templateIdElement = permissionsRoot.get(TemplatePermissions_.template).get(Template_.id);
        Path<Long> roleIdElement = permissionsRoot.get(TemplatePermissions_.role).get(Role_.id);
        Path<TemplatePermission> permissionElement = permissionsRoot.get(TemplatePermissions_.permission);
        queryBody.select(cb.tuple(templateIdElement, roleIdElement, permissionElement));
        queryBody.where(
                cb.or(
                        cb.equal(permissionsRoot.get(TemplatePermissions_.template).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(permissionsRoot.get(TemplatePermissions_.role).get(Role_.project).get(Project_.id), projectId)));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        for (Tuple row : queryResult) {
            Long templateId = row.get(templateIdElement);
            if (!templates.containsKey(templateId)) {
                throw new IllegalStateException("Template{id=" + templateId + "} is not loaded for permission with Role{id=" + row.get(roleIdElement) + "}!");
            }
            Template template = templates.get(templateId);
            Long roleId = row.get(roleIdElement);
            if (!roles.containsKey(roleId)) {
                throw new IllegalStateException("Role{id=" + roleId + "} is not loaded for permission with Template{id=" + templateId + "}!");
            }
            Role role = roles.get(roleId);
            TemplatePermission permission = row.get(permissionElement);
            if (template.permissions == null) {
                template.permissions = new HashMap<>();
            }
            template.permissions.put(role.code, permission);
        }
    }

    private void assembleAttributesToTemplates(Map<Long, Attribute> attributes) {
        attributes.values().stream().forEach(
                (Attribute attribute) -> {
                    Template template = attribute.template;
                    if (template.attributes == null) {
                        template.attributes = new HashMap<>();
                    } else if (template.attributes.containsKey(attribute.code)) {
                        throw new IllegalStateException("Template{id=" + template.id + "} has attributes with duplicated code {" + attribute.code + "}!");
                    }
                    template.attributes.put(attribute.code, attribute);
                }
        );
    }

    private void assembleLinksToTemplates(Map<Long, Link> links) {
        links.values().stream().forEach(
                (Link link) -> {
                    Template template = link.leftTemplate;
                    if (template.links == null) {
                        template.links = new HashMap<>();
                    } else if (template.links.containsKey(link.code)) {
                        throw new IllegalStateException("Template{id=" + template.id + "} has links with duplicated code {" + link.code + "}!");
                    }
                    template.links.put(link.code, link);
                }
        );
    }

    private void assembleStatusesToTemplates(Map<Long, Status> statuses) {
        statuses.values().stream().forEach(
                (Status status) -> {
                    Template template = status.template;
                    if (template.statuses == null) {
                        template.statuses = new HashMap<>();
                    } else if (template.statuses.containsKey(status.code)) {
                        throw new IllegalStateException("Template{id=" + template.id + "} has attributes with duplicated code {" + status.code + "}!");
                    }
                    template.statuses.put(status.code, status);
                }
        );
    }

    private void assembleTransitionsToStatuses(Map<Long, Transition> transitions) {
        transitions.values().stream().forEach(
                (Transition transition) -> {
                    Status sourceStatus = transition.sourceStatus;
                    if (sourceStatus.outcomes == null) {
                        sourceStatus.outcomes = new HashMap<>();
                    } else if (sourceStatus.outcomes.containsKey(transition.destinationStatus.code)) {
                        throw new IllegalStateException("Status{id=" + sourceStatus.id + "} has outcomes with duplicated code {" + transition.destinationStatus.code + "}!");
                    }
                }
        );
    }

    private void loadAttributeGrants(Long projectId, Map<Long, Attribute> attributes, Map<Long, Status> statuses) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<AttributeGrants> grantsRoot = queryBody.from(AttributeGrants.class);
        Path<Long> attributeIdElement = grantsRoot.get(AttributeGrants_.attribute).get(Attribute_.id);
        Path<Long> statusIdElement = grantsRoot.get(AttributeGrants_.status).get(Status_.id);
        Path<ElementPermission> permissionElement = grantsRoot.get(AttributeGrants_.permission);
        queryBody.select(cb.tuple(attributeIdElement, statusIdElement, permissionElement));
        queryBody.where(
                cb.or(
                        cb.equal(grantsRoot.get(AttributeGrants_.attribute).get(Attribute_.template).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(grantsRoot.get(AttributeGrants_.status).get(Status_.template).get(Template_.project).get(Project_.id), projectId)));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        for (Tuple row : queryResult) {
            Long attributeId = row.get(attributeIdElement);
            if (!attributes.containsKey(attributeId)) {
                throw new IllegalStateException("Attibute{id=" + attributeId + "} is not loaded for permission with Status{id=" + row.get(statusIdElement) + "}!");
            }
            Attribute attribute = attributes.get(attributeId);
            Long statusId = row.get(statusIdElement);
            if (!statuses.containsKey(statusId)) {
                throw new IllegalStateException("Status{id=" + statusId + "} is not loaded for permission with Attribute{id=" + attributeId + "}");
            }
            Status status = statuses.get(statusId);
            ElementPermission permission = row.get(permissionElement);
            if (attribute.statusPermissions == null) {
                attribute.statusPermissions = new HashMap<>();
            }
            attribute.statusPermissions.put(status.code, permission);
        }
    }

    private void loadLinkGrants(Long projectId, Map<Long, Link> links, Map<Long, Status> statuses) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<LinkGrants> grantsRoot = queryBody.from(LinkGrants.class);
        Path<Long> linkIdElement = grantsRoot.get(LinkGrants_.link).get(Link_.id);
        Path<Long> statusIdElement = grantsRoot.get(LinkGrants_.status).get(Status_.id);
        Path<ElementPermission> permissionElement = grantsRoot.get(LinkGrants_.permission);
        queryBody.select(cb.tuple(linkIdElement, statusIdElement, permissionElement));
        queryBody.where(
                cb.or(
                        cb.equal(grantsRoot.get(LinkGrants_.link).get(Link_.leftTemplate).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(grantsRoot.get(LinkGrants_.link).get(Link_.rightTemplate).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(grantsRoot.get(LinkGrants_.status).get(Status_.template).get(Template_.project).get(Project_.id), projectId)));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        for (Tuple row : queryResult) {
            Long linkId = row.get(linkIdElement);
            if (!links.containsKey(linkId)) {
                throw new IllegalStateException("Link{id=" + linkId + "} is not loaded for permission with Status{id=" + row.get(statusIdElement) + "}!");
            }
            Link link = links.get(linkId);
            Long statusId = row.get(statusIdElement);
            if (!statuses.containsKey(statusId)) {
                throw new IllegalStateException("Status{id=" + statusId + "} is not loaded for permission with Attribute{id=" + linkId + "}");
            }
            Status status = statuses.get(statusId);
            ElementPermission permission = row.get(permissionElement);
            if (status.template.equals(link.leftTemplate)) {
                if (link.leftStatusPermissions == null) {
                    link.leftStatusPermissions = new HashMap<>();
                }
                link.leftStatusPermissions.put(status.code, permission);
            } else {
                if (link.rightStatusPermissions == null) {
                    link.rightStatusPermissions = new HashMap<>();
                }
                link.rightStatusPermissions.put(status.code, permission);
            }
        }
    }

    private void loadAttributePermissions(Long projectId, Map<Long, Attribute> attributes, Map<Long, Role> roles) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<AttributePermissions> permissionsRoot = queryBody.from(AttributePermissions.class);
        Path<Long> attributeIdElement = permissionsRoot.get(AttributePermissions_.attribute).get(Attribute_.id);
        Path<Long> roleIdElement = permissionsRoot.get(AttributePermissions_.role).get(Role_.id);
        Path<ElementPermission> permissionElement = permissionsRoot.get(AttributePermissions_.permission);
        queryBody.select(cb.tuple(attributeIdElement, roleIdElement, permissionElement));
        queryBody.where(
                cb.or(
                        cb.equal(permissionsRoot.get(AttributePermissions_.attribute).get(Attribute_.template).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(permissionsRoot.get(AttributePermissions_.role).get(Role_.project).get(Project_.id), projectId)));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        for (Tuple row : queryResult) {
            Long attributeId = row.get(attributeIdElement);
            if (!attributes.containsKey(attributeId)) {
                throw new IllegalStateException("Attibute{id=" + attributeId + "} is not loaded for permission with Role{id=" + row.get(roleIdElement) + "}!");
            }
            Attribute attribute = attributes.get(attributeId);
            Long roleId = row.get(roleIdElement);
            if (!roles.containsKey(roleId)) {
                throw new IllegalStateException("Role{id=" + roleId + "} is not loaded for permission with Attribute{id=" + attributeId + "}");
            }
            Role role = roles.get(roleId);
            ElementPermission permission = row.get(permissionElement);
            if (attribute.rolePermissions == null) {
                attribute.rolePermissions = new HashMap<>();
            }
            attribute.rolePermissions.put(role.code, permission);
        }
    }

    private void loadLinkPermissions(Long projectId, Map<Long, Link> links, Map<Long, Role> roles) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<LinkPermissions> permissionsRoot = queryBody.from(LinkPermissions.class);
        Path<Long> linkIdElement = permissionsRoot.get(LinkPermissions_.link).get(Link_.id);
        Path<Long> roleIdElement = permissionsRoot.get(LinkPermissions_.role).get(Role_.id);
        Path<ElementPermission> permissionElement = permissionsRoot.get(LinkPermissions_.permission);
        queryBody.select(cb.tuple(linkIdElement, roleIdElement, permissionElement));
        queryBody.where(
                cb.or(
                        cb.equal(permissionsRoot.get(LinkPermissions_.link).get(Link_.leftTemplate).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(permissionsRoot.get(LinkPermissions_.link).get(Link_.rightTemplate).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(permissionsRoot.get(LinkPermissions_.role).get(Role_.project).get(Project_.id), projectId)));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        for (Tuple row : queryResult) {
            Long linkId = row.get(linkIdElement);
            if (!links.containsKey(linkId)) {
                throw new IllegalStateException("Attibute{id=" + linkId + "} is not loaded for permission with Status{id=" + row.get(roleIdElement) + "}!");
            }
            Link link = links.get(linkId);
            Long roleId = row.get(roleIdElement);
            if (!roles.containsKey(roleId)) {
                throw new IllegalStateException("Status{id=" + roleId + "} is not loaded for permission with Attribute{id=" + linkId + "}");
            }
            Role role = roles.get(roleId);
            ElementPermission permission = row.get(permissionElement);
            if (link.rolePermissions == null) {
                link.rolePermissions = new HashMap<>();
            }
            link.rolePermissions.put(role.code, permission);
        }
    }

    private void loadTransitionPermissions(Long projectId, Map<Long, Transition> transitions, Map<Long, Role> roles) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<ru.skycelot.metamodel.persistence.Transition> transitionRoot = queryBody.from(ru.skycelot.metamodel.persistence.Transition.class);
        Join<ru.skycelot.metamodel.persistence.Transition, ru.skycelot.metamodel.persistence.Role> roleJoin = transitionRoot.join(Transition_.roles);
        Path<Long> transitionIdElement = transitionRoot.get(Transition_.id);
        Path<Long> roleIdElement = roleJoin.get(Role_.id);
        queryBody.select(cb.tuple(transitionIdElement, roleIdElement));
        queryBody.where(
                cb.or(
                        cb.equal(transitionRoot.get(Transition_.sourceStatus).get(Status_.template).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(transitionRoot.get(Transition_.destinationStatus).get(Status_.template).get(Template_.project).get(Project_.id), projectId),
                        cb.equal(roleJoin.get(Role_.project).get(Project_.id), projectId)));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        for (Tuple row : queryResult) {
            Long transitionId = row.get(transitionIdElement);
            if (!transitions.containsKey(transitionId)) {
                throw new IllegalStateException();
            }
            Transition transition = transitions.get(transitionId);
            Long roleId = row.get(roleIdElement);
            if (!roles.containsKey(roleId)) {
                throw new IllegalStateException();
            }
            Role role = roles.get(roleId);
            if (transition.rolePermissions == null) {
                transition.rolePermissions = new HashMap<>();
            }
            transition.rolePermissions.put(role.code, role);
        }
    }

    public List<User> loadUsers(Project project) {
        Map<Long, User> result = loadUserProfiles(project);
        Map<Long, Role> roles = loadProjectRoles(project);
        loadUserRoles(project.id, result, roles);
        return new ArrayList<>(result.values());
    }

    private Map<Long, User> loadUserProfiles(Project project) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<ru.skycelot.metamodel.persistence.Profile> profileRoot = queryBody.from(ru.skycelot.metamodel.persistence.Profile.class);
        Path<Long> userIdElement = profileRoot.get(Profile_.user).get(User_.id);
        Path<Long> profileIdElement = profileRoot.get(Profile_.id);
        Path<String> userFullNameElement = profileRoot.get(Profile_.user).get(User_.fullName);
        Path<String> userLoginElement = profileRoot.get(Profile_.user).get(User_.fullName);
        Path<String> userPasswordElement = profileRoot.get(Profile_.user).get(User_.fullName);
        queryBody.select(cb.tuple(userIdElement, profileIdElement, userLoginElement, userPasswordElement));
        queryBody.where(cb.equal(profileRoot.get(Profile_.project).get(Project_.id), project.id));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        Map<Long, User> result;
        if (queryResult.isEmpty()) {
            result = Collections.emptyMap();
        } else {
            result = new HashMap<>(queryResult.size() * 2);
            for (Tuple row : queryResult) {
                User user = new User();
                Profile profile = new Profile();
                user.profile = profile;
                profile.id = row.get(profileIdElement);
                profile.project = project;
                user.id = row.get(userIdElement);
                user.fullName = row.get(userFullNameElement);
                user.login = row.get(userLoginElement);
                user.password = row.get(userPasswordElement);
                result.put(profile.id, user);
            }
        }
        return result;
    }

    private void loadUserRoles(Long projectId, Map<Long, User> userProfiles, Map<Long, Role> roles) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> queryBody = cb.createTupleQuery();
        Root<ru.skycelot.metamodel.persistence.Profile> profileRoot = queryBody.from(ru.skycelot.metamodel.persistence.Profile.class);
        Join<ru.skycelot.metamodel.persistence.Profile, ru.skycelot.metamodel.persistence.Role> roleJoin = profileRoot.join(Profile_.roles);
        Path<Long> profileIdElement = profileRoot.get(Profile_.id);
        Path<Long> roleIdElement = roleJoin.get(Role_.id);
        queryBody.select(cb.tuple(profileIdElement, roleIdElement));
        queryBody.where(
                cb.or(
                        cb.equal(profileRoot.get(Profile_.project).get(Project_.id), projectId),
                        cb.equal(roleJoin.get(Role_.project).get(Project_.id), projectId)));
        TypedQuery<Tuple> query = em.createQuery(queryBody);
        List<Tuple> queryResult = query.getResultList();
        for (Tuple row : queryResult) {
            Long profileId = row.get(profileIdElement);
            if (!userProfiles.containsKey(profileId)) {
                throw new IllegalStateException("Profile{id=" + profileId + "} is not loaded for Role{id=" + row.get(roleIdElement) + "}!");
            }
            Profile profile = userProfiles.get(profileId).profile;
            Long roleId = row.get(roleIdElement);
            if (!roles.containsKey(roleId)) {
                throw new IllegalStateException("Role{id=" + roleId + "} is not loaded for Profile{id=" + profileId + "}!");
            }
            Role role = roles.get(roleId);
            if (profile.roles == null) {
                profile.roles = new HashMap<>();
            }
            profile.roles.put(role.code, role);
        }
    }
}
