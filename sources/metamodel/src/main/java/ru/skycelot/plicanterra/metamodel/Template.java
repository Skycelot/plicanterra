package ru.skycelot.plicanterra.metamodel;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import ru.skycelot.plicanterra.util.orm.Filter;
import ru.skycelot.plicanterra.util.orm.JoinTableColumn;

/**
 *
 */
@Table(name = "template")
public class Template {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "project")
    @MapKey(name = "code")
    private Map<String, Attribute> attributes;
    @OneToMany(mappedBy = "project")
    @MapKey(name = "code")
    private Map<String, Link> links;
    @OneToMany(mappedBy = "project")
    @MapKey(name = "code")
    private Map<String, Status> statuses;
    @OneToMany(mappedBy = "project")
    @Filter(field = "initial", contextual = false, value = "true")
    private Status initialStatus;
    @ManyToMany(targetEntity = Role.class)
    @MapKey(name = "code")
    @JoinTable(name = "template_permissions",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JoinTableColumn(name = "permission")
    @Enumerated(EnumType.STRING)
    private Map<String, TemplatePermission> permissions;
    @JoinColumn(name = "project_id")
    private Project project;

    public Template() {
    }

    public Template(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    public Map<String, Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(Map<String, Status> statuses) {
        this.statuses = statuses;
    }

    public Status getInitialStatus() {
        return initialStatus;
    }

    public void setInitialStatus(Status initialStatus) {
        this.initialStatus = initialStatus;
    }

    public Map<String, TemplatePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, TemplatePermission> permissions) {
        this.permissions = permissions;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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
        return "Template{" + "code=" + code + ", project=" + project + '}';
    }
}
