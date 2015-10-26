package ru.skycelot.plicanterra.metamodel;

import java.util.Map;

/**
 *
 */
public class Link {

    public Long id;
    public Template leftTemplate;
    public Template rightTemplate;
    public String code;
    public String name;
    public String description;
    public LinkType type;
    public Map<String, ElementPermission> statusPermissions;
    public Map<String, ElementPermission> rolePermissions;

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Link) {
            final Link other = (Link) obj;
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
        return "Link{" + "code=" + code + ", leftTemplate=" + leftTemplate + ", rightTemplate=" + rightTemplate + '}';
    }
}
