package ru.skycelot.metamodel.persistence;

import java.io.Serializable;

/**
 *
 */
public class TemplatePermissionsId implements Serializable {

    private long template;
    private long role;

    public long getTemplate() {
        return template;
    }

    public void setTemplate(long template) {
        this.template = template;
    }

    public long getRole() {
        return role;
    }

    public void setRole(long role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(template) + Long.hashCode(role);
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof TemplatePermissionsId) {
            TemplatePermissionsId other = (TemplatePermissionsId) obj;
            if (template == other.template && role == other.role) {
                equality = true;
            }
        }
        return equality;
    }
}
