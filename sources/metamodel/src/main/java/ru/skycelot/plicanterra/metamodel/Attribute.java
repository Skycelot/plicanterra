package ru.skycelot.plicanterra.metamodel;

import java.util.Map;

/**
 *
 */
public class Attribute {

    public Long id;
    public Template template;
    public String code;
    public String name;
    public String description;
    public AttributeType type;
    public Map<String, ElementPermission> statusPermissions;
    public Map<String, ElementPermission> rolePermissions;

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Attribute) {
            final Attribute other = (Attribute) obj;
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
        return "Attribute{" + "code=" + code + ", template=" + template + '}';
    }
}
