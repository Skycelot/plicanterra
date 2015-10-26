package ru.skycelot.plicanterra.metamodel;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.Table;

/**
 *
 */
@Table(name = "transition")
public class Transition {

    @Id
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "status_source_id")
    private Status sourceStatus;
    @ManyToOne
    @JoinColumn(name = "status_destination_id")
    private Status destinationStatus;
    @ManyToMany
    @MapKey(name = "code")
    @JoinTable(name = "transition_permissions",
            joinColumns = @JoinColumn(name = "transition_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Map<String, Role> rolePermissions;

    public Transition() {
    }

    public Transition(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(Status sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public Status getDestinationStatus() {
        return destinationStatus;
    }

    public void setDestinationStatus(Status destinationStatus) {
        this.destinationStatus = destinationStatus;
    }

    public Map<String, Role> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(Map<String, Role> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Transition) {
            final Transition other = (Transition) obj;
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
        return "Transition{" + "sourceStatus=" + sourceStatus + ", destinationStatus=" + destinationStatus + '}';
    }
}
