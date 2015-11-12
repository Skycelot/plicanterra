package ru.skycelot.metamodel.persistence;

import java.io.Serializable;

/**
 *
 */
public class AttributePermissionsId implements Serializable {

    private long attribute;
    private long role;

    public long getAttribute() {
        return attribute;
    }

    public void setAttribute(long attribute) {
        this.attribute = attribute;
    }

    public long getRole() {
        return role;
    }

    public void setRole(long role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(attribute) + Long.hashCode(role);
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof AttributePermissionsId) {
            AttributePermissionsId other = (AttributePermissionsId) obj;
            if (attribute == other.attribute && role == other.role) {
                equality = true;
            }
        }
        return equality;
    }
}
