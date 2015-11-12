package ru.skycelot.metamodel.persistence;

import java.io.Serializable;

/**
 *
 */
public class LinkPermissionsId implements Serializable {

    private long link;
    private long role;

    public long getLink() {
        return link;
    }

    public void setLink(long link) {
        this.link = link;
    }

    public long getRole() {
        return role;
    }

    public void setRole(long role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(link) + Long.hashCode(role);
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof LinkPermissionsId) {
            LinkPermissionsId other = (LinkPermissionsId) obj;
            if (link == other.link && role == other.role) {
                equality = true;
            }
        }
        return equality;
    }
}
