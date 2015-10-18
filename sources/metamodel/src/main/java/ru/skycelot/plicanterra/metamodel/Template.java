package ru.skycelot.plicanterra.metamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.skycelot.plicanterra.util.Argument;
import ru.skycelot.plicanterra.util.ArgumentsChecker;

/**
 *
 */
public class Template {

    public Long id;
    public String code;
    public String name;
    public String desc;
    public Map<String, Attribute> attributes;
    public Map<String, Link> links;
    public Status statusGraph;
    public Map<String, Role> usableBy;
    public Map<String, Role> creatableBy;
    public Map<String, Role> removableBy;
    public Project project;

    public static Map<Long, Template> loadTemplates(Connection connection, Project project) {
        ArgumentsChecker.notNull(new Argument("connection", connection), new Argument("project", project));
        Map<Long, Template> result = null;
        String templatesQuery = "select t.ID, t.CODE, t.NAME, t.DESCRIPTION from TEMPLATE t where t.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(templatesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, project.id);
            ResultSet resultSet = statement.executeQuery();
            boolean notEmpty = resultSet.last();
            if (notEmpty) {
                int count = resultSet.getRow();
                resultSet.beforeFirst();
                result = new HashMap<>(count * 2);
                while (resultSet.next()) {
                    Template template = new Template();
                    template.id = resultSet.getLong("ID");
                    template.code = resultSet.getString("CODE");
                    template.name = resultSet.getString("NAME");
                    template.desc = resultSet.getString("DESCRIPTION");
                    template.project = project;
                    result.put(template.id, template);
                }
            } else {
                result = new HashMap<>();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void loadRolePermissions(Connection connection, Long projectId, Map<Long, Template> templates, Map<Long, Role> roles) {
        ArgumentsChecker.notNull(new Argument("connection", connection), new Argument("projectId", projectId));
        if (templates != null && !templates.isEmpty() && roles != null && !roles.isEmpty()) {
            String attributesQuery = "select distinct tp.TEMPLATE_ID, tp.ROLE_ID, p.CODE from TEMPLATE_PERMISSIONS tp inner join TEMPLATE t on tp.TEMPLATE_ID = t.ID inner join PERMISSION p on tp.PERMISSION_ID = p.ID where t.PROJECT_ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(attributesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
                statement.setLong(1, projectId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    long templateId = resultSet.getLong("TEMPLATE_ID");
                    if (templates.containsKey(templateId)) {
                        Template template = templates.get(templateId);
                        long roleId = resultSet.getLong("ROLE_ID");
                        if (roles.containsKey(roleId)) {
                            Role role = roles.get(roleId);
                            String accessCode = resultSet.getString("CODE");
                            if (accessCode != null) {
                                TemplatePermission accessType = TemplatePermission.valueOf(accessCode);
                                switch (accessType) {
                                    case DELETE:
                                        template.removableBy.put(role.code, role);
                                    case CREATE:
                                        template.creatableBy.put(role.code, role);
                                    case USE:
                                        template.usableBy.put(role.code, role);
                                }
                            } else {
                                throw new IllegalStateException("Template permission for templateId {" + templateId + "} and roleId {" + roleId + "} is empty!");
                            }
                        } else {
                            throw new IllegalStateException("There is template permission for absent role{id=" + roleId + "}");
                        }
                    } else {
                        throw new IllegalStateException("There is template permission for absent template{id=" + templateId + "}");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void gatherTemplatesAttributes(Map<Long, Attribute> attributes) {
        for (Attribute attribute : attributes.values()) {
            Template template = attribute.template;
            if (template != null) {
                if (template.attributes == null) {
                    template.attributes = new HashMap<>();
                }
                if (template.attributes.containsKey(attribute.code)) {
                    throw new IllegalStateException("Duplicate attributes with the code {" + attribute.code + "} for the template {code=" + attribute.template.code + "}");
                }
                template.attributes.put(attribute.code, attribute);
            } else {
                throw new IllegalStateException("Attribute{id=" + attribute.id + "} doesn't have a loaded template!");
            }
        }
    }

    public static void gatherTemplatesLinks(Map<Long, Link> links) {
        for (Link link : links.values()) {
            Template template1 = link.templateA;
            Template template2 = link.templateB;
            if (template1.links == null) {
                template1.links = new HashMap<>();
            }
            template1.links.put(link.code, link);
            if (template2.links == null) {
                template2.links = new HashMap<>();
            }
            template2.links.put(link.code, link);
        }
    }

    public static void gatherTemplatesStatusGraphs(Map<Long, Template> templates, Map<Long, Status> statuses) {
        Set<Long> unstatusedTemplates = new HashSet<>(templates.keySet());
        for (Status status : statuses.values()) {
            if (status.lifecyclePhase == StatusStage.INITIAL) {
                Template template = status.template;
                if (template.statusGraph != null) {
                    throw new IllegalStateException("Duplicate initial statuses for the Template{id=" + template.id + "}");
                }
                template.statusGraph = status;
                unstatusedTemplates.remove(template.id);
            }
        }
        if (!unstatusedTemplates.isEmpty()) {
            throw new IllegalStateException("There is no initial statuses for templates with id=" + unstatusedTemplates);
        }
    }

    public Status findStatus(String code) {
        Status result = statusGraph.findStatus(code);
        if (result != null) {
            return result;
        } else {
            throw new IllegalStateException("Template{id=" + id + "} doesn't have a Status{code= " + code + "}");
        }
    }

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Template) {
            final Template other = (Template) obj;
            if (this.id != null) {
                equality = this.id.equals(other.id);
            } else {
                equality = super.equals(obj);
            }
        }
        return equality;
    }

    @Override
    public String toString() {
        return "Template{" + "code=" + code + '}';
    }
}
