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
@Table(name = "attribute")
public class Attribute {

    @Id
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AttributeType type;
    @ManyToMany(targetEntity = Status.class)
    @MapKey(name = "code")
    @JoinTable(name = "attribute_grants",
            joinColumns = @JoinColumn(name = "attribute_id"),
            inverseJoinColumns = @JoinColumn(name = "status_id"))
    @JoinTableColumn(name = "permission")
    @Enumerated(EnumType.STRING)
    private Map<String, ElementPermission> statusPermissions;
    @ManyToMany(targetEntity = Role.class)
    @MapKey(name = "code")
    @JoinTable(name = "attribute_permissions",
            joinColumns = @JoinColumn(name = "attribute_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JoinTableColumn(name = "permission")
    @Enumerated(EnumType.STRING)
    private Map<String, ElementPermission> rolePermissions;

    public Attribute() {
    }

    public Attribute(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
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

    public void setDescription(String desc) {
        this.description = desc;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
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
        return "Attribute{" + "code=" + code + ", template=" + template + '}';
    }
}
