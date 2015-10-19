package ru.skycelot.plicanterra.metamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Attribute {

    public Long id;
    public Template template;
    public String code;
    public String name;
    public String desc;
    public AttributeType type;
    public Map<String, Role> visibleFor;
    public Map<String, Status> visibleIn;
    public Map<String, Role> editableFor;
    public Map<String, Status> editableIn;

    public static Map<Long, Attribute> loadAttributes(Connection connection, Long projectId, Map<Long, Template> templates) {
        String attributesQuery = "select a.ID, a.TEMPLATE_ID, a.CODE, a.NAME, a.DESCRIPTION, ty.CODE as TYPE_CODE from ATTRIBUTE a inner join TEMPLATE t on a.TEMPLATE_ID = t.ID left join ATTRIBUTE_TYPE ty on a.ATTRIBUTE_TYPE_ID = ty.ID where t.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(attributesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            Map<Long, Attribute> result;
            boolean notEmpty = resultSet.last();
            if (notEmpty) {
                int count = resultSet.getRow();
                resultSet.beforeFirst();
                result = new HashMap<>((int) (count / 0.75) + 100);
                while (resultSet.next()) {
                    Attribute attribute = new Attribute();
                    attribute.id = resultSet.getLong("ID");
                    long templateId = resultSet.getLong("TEMPLATE_ID");
                    if (templates.containsKey(templateId)) {
                        attribute.template = templates.get(templateId);
                        attribute.code = resultSet.getString("CODE");
                        attribute.name = resultSet.getString("NAME");
                        attribute.desc = resultSet.getString("DESCRIPTION");
                        String typeCode = resultSet.getString("TYPE_CODE");
                        if (typeCode != null) {
                            attribute.type = AttributeType.valueOf(typeCode);
                            result.put(attribute.id, attribute);
                        } else {
                            throw new IllegalStateException("Attribute{id=" + attribute.id + "}'s type doesn't have code!");
                        }
                    } else {
                        throw new IllegalStateException("Attribute{id=" + attribute.id + "}'s template isn't loaded!");
                    }
                }
            } else {
                result = new HashMap<>();
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadStatusPermissions(Connection connection, Long projectId, Map<Long, Attribute> attributes, Map<Long, Status> statuses) {
        String attributesQuery = "select distinct ac.ATTRIBUTE_ID, ac.STATUS_ID, p.CODE from ATTRIBUTE_GRANTS ac inner join STATUS s on ac.STATUS_ID = s.ID left join PERMISSION p on ac.PERMISSION_ID = p.ID inner join TEMPLATE t on s.TEMPLATE_ID = t.ID where t.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(attributesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Attribute attribute = attributes.get(resultSet.getLong("ATTRIBUTE_ID"));
                Status status = statuses.get(resultSet.getLong("STATUS_ID"));
                AttributePermission type = AttributePermission.valueOf(resultSet.getString("CODE"));
                switch (type) {
                    case WRITE:
                        if (attribute.editableIn == null) {
                            attribute.editableIn = new HashMap<>();
                        }
                        attribute.editableIn.put(status.code, status);
                    case READ:
                        if (attribute.visibleIn == null) {
                            attribute.visibleIn = new HashMap<>();
                        }
                        attribute.visibleIn.put(status.code, status);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadRolePermissions(Connection connection, Long projectId, Map<Long, Attribute> attributes, Map<Long, Role> roles) {
        String attributesQuery = "select distinct ac.ATTRIBUTE_ID, ac.ROLE_ID, p.CODE from ATTRIBUTE_PERMISSIONS ac inner join PERMISSION p on ac.PERMISSION_ID = p.ID inner join ATTRIBUTE a on ac.ATTRIBUTE_ID = a.ID inner join TEMPLATE t on a.TEMPLATE_ID = t.ID where t.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(attributesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Attribute attribute = attributes.get(resultSet.getLong("ATTRIBUTE_ID"));
                Role role = roles.get(resultSet.getLong("ROLE_ID"));
                AttributePermission type = AttributePermission.valueOf(resultSet.getString("CODE"));
                switch (type) {
                    case WRITE:
                        if (attribute.editableFor == null) {
                            attribute.editableFor = new HashMap<>();
                        }
                        attribute.editableFor.put(role.code, role);
                    case READ:
                        if (attribute.visibleFor == null) {
                            attribute.visibleFor = new HashMap<>();
                        }
                        attribute.visibleFor.put(role.code, role);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Attribute) {
            final Attribute other = (Attribute) obj;
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
        return "Attribute{" + "code=" + code + '}' + " of template{" + "code=" + template.code + "}";
    }
}
