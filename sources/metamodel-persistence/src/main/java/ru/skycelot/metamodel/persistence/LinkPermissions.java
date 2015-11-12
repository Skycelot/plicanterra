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
import ru.skycelot.plicanterra.metamodel.ElementPermission;

/**
 *
 */
@Entity
@Table(name = "link_permissions")
@IdClass(LinkPermissionsId.class)
public class LinkPermissions {

    @Id
    @ManyToOne
    @JoinColumn(name = "link_id")
    private Link link;
    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private ElementPermission permission;

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ElementPermission getPermission() {
        return permission;
    }

    public void setPermission(ElementPermission permission) {
        this.permission = permission;
    }
}
