package ru.skycelot.metamodel.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import ru.skycelot.plicanterra.metamodel.TemplatePermission;

/**
 *
 */
@Entity
@Table(name = "template_permissions")
@IdClass(TemplatePermissionsId.class)
public class TemplatePermissions {

    @Id
    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;
    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private TemplatePermission permission;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public TemplatePermission getPermission() {
        return permission;
    }

    public void setPermission(TemplatePermission permission) {
        this.permission = permission;
    }
}
