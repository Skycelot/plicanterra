package ru.skycelot.plicanterra.metamodel;

import java.util.Map;

/**
 *
 */
public class Transition {

    public Long id;
    public Status sourceStatus;
    public Status destinationStatus;
    public Map<String, Role> rolePermissions;

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
