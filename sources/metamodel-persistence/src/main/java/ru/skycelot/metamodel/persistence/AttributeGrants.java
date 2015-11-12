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
@Table(name = "attribute_grants")
@IdClass(AttributeGrantsId.class)
public class AttributeGrants {

    @Id
    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;
    @Id
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private ElementPermission permission;

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ElementPermission getPermission() {
        return permission;
    }

    public void setPermission(ElementPermission permission) {
        this.permission = permission;
    }
}
