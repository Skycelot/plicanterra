package ru.skycelot.plicanterra.metamodel;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.Table;
import ru.skycelot.plicanterra.util.orm.JoinTableColumn;

/**
 *
 */
@Table(name = "link")
public class Link {

    @Id
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "template_left_id")
    private Template leftTemplate;
    @ManyToOne
    @JoinColumn(name = "template_right_id")
    private Template rightTemplate;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private LinkType type;
    @ManyToMany(targetEntity = Status.class)
    @MapKey(name = "code")
    @JoinTable(name = "link_grants",
            joinColumns = @JoinColumn(name = "link_id"),
            inverseJoinColumns = @JoinColumn(name = "status_id"))
    @JoinTableColumn(name = "permission")
    @Enumerated(EnumType.STRING)
    private Map<String, ElementPermission> statusPermissions;
    @ManyToMany(targetEntity = Role.class)
    @MapKey(name = "code")
    @JoinTable(name = "link_permissions",
            joinColumns = @JoinColumn(name = "link_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JoinTableColumn(name = "permission")
    @Enumerated(EnumType.STRING)
    private Map<String, ElementPermission> rolePermissions;

    public Link() {
    }

    public Link(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Template getLeftTemplate() {
        return leftTemplate;
    }

    public void setLeftTemplate(Template leftTemplate) {
        this.leftTemplate = leftTemplate;
    }

    public Template getRightTemplate() {
        return rightTemplate;
    }

    public void setRightTemplate(Template rightTemplate) {
        this.rightTemplate = rightTemplate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkType getType() {
        return type;
    }

    public void setType(LinkType type) {
        this.type = type;
    }

    public Map<String, ElementPermission> getStatusPermissions() {
        return statusPermissions;
    }

    public void setStatusPermissions(Map<String, ElementPermission> statusPermissions) {
        this.statusPermissions = statusPermissions;
    }

    public Map<String, ElementPermission> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(Map<String, ElementPermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
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
        return "Link{" + "code=" + code + ", leftTemplate=" + leftTemplate + ", rightTemplate=" + rightTemplate + '}';
    }
}
