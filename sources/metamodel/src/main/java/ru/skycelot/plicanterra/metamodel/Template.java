package ru.skycelot.plicanterra.metamodel;

import java.util.Map;

/**
 *
 */
public class Template {

    public Long id;
    public String code;
    public String name;
    public String description;
    public Map<String, Attribute> attributes;
    public Map<String, Link> links;
    public Map<String, Status> statuses;
    public Status initialStatus;
    public Map<String, TemplatePermission> permissions;
    public Project project;

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Template) {
            final Template other = (Template) obj;
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
        return "Template{" + "code=" + code + ", project=" + project + '}';
    }
}
