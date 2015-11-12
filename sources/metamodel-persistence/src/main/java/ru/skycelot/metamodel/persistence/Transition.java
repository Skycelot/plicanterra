package ru.skycelot.metamodel.persistence;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "transition")
public class Transition {

    @Id
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "source_status_id")
    private Status sourceStatus;
    @ManyToOne
    @JoinColumn(name = "destination_status_id")
    private Status destinationStatus;
    @ManyToMany
    @JoinTable(name = "transition_permissions",
            joinColumns = @JoinColumn(name = "transition_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
