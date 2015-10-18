package ru.skycelot.plicanterra.metamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class Link {

    public Long id;
    public Template templateA;
    public Template templateB;
    public String code;
    public String name;
    public String desc;
    public LinkType type;
    public Map<String, Role> visibleFor;
    public Map<String, Status> visibleAsAIn;
    public Map<String, Status> visibleAsBIn;
    public Map<String, Role> editableFor;
    public Map<String, Status> editableAsAIn;
    public Map<String, Status> editableAsBIn;

    public static Map<Long, Link> loadLinks(Connection connection, Long projectId, Map<Long, Template> templates) {
        Map<Long, Link> result = null;
        String linksQuery = "select l.ID, l.TEMPLATE_1_ID, l.TEMPLATE_2_ID, l.CODE, l.NAME, l.DESCRIPTION, ty.CODE as TYPE_CODE from LINK l inner join TEMPLATE t1 on l.TEMPLATE_1_ID = t1.ID inner join TEMPLATE t2 on l.TEMPLATE_2_ID = t2.ID inner join LINK_TYPE ty on l.LINK_TYPE_ID = ty.ID where t1.PROJECT_ID = ? and t2.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(linksQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, projectId);
            statement.setLong(2, projectId);
            ResultSet resultSet = statement.executeQuery();
            boolean notEmpty = resultSet.last();
            if (notEmpty) {
                int count = resultSet.getRow();
                resultSet.beforeFirst();
                result = new HashMap<>(count * 2);
                Set<String> linkCodes = new HashSet<>(count * 2);
                while (resultSet.next()) {
                    Link link = new Link();
                    link.id = resultSet.getLong("ID");
                    long templateLeftId = resultSet.getLong("TEMPLATE_1_ID");
                    if (templates.containsKey(templateLeftId)) {
                        link.templateA = templates.get(templateLeftId);
                        long templateRightId = resultSet.getLong("TEMPLATE_2_ID");
                        if (templates.containsKey(templateRightId)) {
                            link.templateB = templates.get(templateRightId);
                            link.code = resultSet.getString("CODE");
                            if (linkCodes.contains(link.code)) {
                                throw new IllegalStateException("Duplicate links with the code {" + link.code + "}");
                            }
                            linkCodes.add(link.code);
                            link.name = resultSet.getString("NAME");
                            link.desc = resultSet.getString("DESCRIPTION");
                            String typeCode = resultSet.getString("TYPE_CODE");
                            if (typeCode != null) {
                                link.type = LinkType.valueOf(typeCode);
                                result.put(link.id, link);
                            } else {
                                throw new IllegalStateException("Link{id=" + link.id + "}'s type has no code!");
                            }
                        } else {
                            throw new IllegalStateException("Link{id=" + link.id + "}'s right template isn't loaded!");
                        }
                    } else {
                        throw new IllegalStateException("Link{id=" + link.id + "}'s left template isn't loaded!");
                    }
                }
            } else {
                result = new HashMap<>();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void loadStatusPermissions(Connection connection, Long projectId, Map<Long, Link> links, Map<Long, Status> statuses) {
        String linksQuery = "select distinct lc.LINK_ID, lc.STATUS_ID, p.CODE from LINK_GRANTS lc inner join PERMISSION P on lc.PERMISSION_ID = p.ID inner join STATUS s on lc.STATUS_ID = s.ID inner join TEMPLATE t on s.TEMPLATE_ID = t.ID where t.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(linksQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
            statement.setLong(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Link link = links.get(resultSet.getLong("LINK_ID"));
                Status status = statuses.get(resultSet.getLong("STATUS_ID"));
                AttributePermission type = AttributePermission.valueOf(resultSet.getString("CODE"));
                Map<String, Status> permissionMapForEdit = null;
                Map<String, Status> permissionMapForView = null;
                if (status.template.equals(link.templateA)) {
                    if (link.editableAsAIn == null) {
                        link.editableAsAIn = new HashMap<>();
                    }
                    if (link.visibleAsAIn == null) {
                        link.visibleAsAIn = new HashMap<>();
                    }
                    permissionMapForEdit = link.editableAsAIn;
                    permissionMapForView = link.visibleAsAIn;
                } else {
                    if (link.editableAsBIn == null) {
                        link.editableAsBIn = new HashMap<>();
                    }
                    if (link.visibleAsBIn == null) {
                        link.visibleAsBIn = new HashMap<>();
                    }
                    permissionMapForEdit = link.editableAsBIn;
                    permissionMapForView = link.visibleAsBIn;
                }
                switch (type) {
                    case WRITE:
                        permissionMapForEdit.put(status.code, status);
                    case READ:
                        permissionMapForView.put(status.code, status);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadRolePermissions(Connection connection, Long projectId, Map<Long, Link> links, Map<Long, Role> roles) {
        String linksQuery = "select distinct lc.LINK_ID, lc.ROLE_ID, p.CODE from LINK_PERMISSIONS lc inner join PERMISSION p on lc.PERMISSION_ID = p.ID inner join LINK l on lc.LINK_ID = l.ID inner join TEMPLATE tA on l.TEMPLATE_1_ID = tA.ID inner join TEMPLATE tB on l.TEMPLATE_2_ID = tB.ID where tA.PROJECT_ID = ? and tB.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(linksQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
            statement.setLong(1, projectId);
            statement.setLong(2, projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Link link = links.get(resultSet.getLong("LINK_ID"));
                Role role = roles.get(resultSet.getLong("ROLE_ID"));
                AttributePermission type = AttributePermission.valueOf(resultSet.getString("CODE"));
                switch (type) {
                    case WRITE:
                        if (link.editableFor == null) {
                            link.editableFor = new HashMap<>();
                        }
                        link.editableFor.put(role.code, role);
                    case READ:
                        if (link.visibleFor == null) {
                            link.visibleFor = new HashMap<>();
                        }
                        link.visibleFor.put(role.code, role);
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
        if (obj instanceof Link) {
            final Link other = (Link) obj;
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
        return "Link{" + "code=" + code + '}' + " of templates{" + "codeA=" + templateA.code + ",codeB=" + templateB.code + "}";
    }
}
