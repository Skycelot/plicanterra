package ru.skycelot.plicanterra.metamodel;

import java.util.Map;

/**
 *
 */
public class Project {

    public Long id;
    public String code;
    public String name;
    public String description;
    public Map<String, Template> templates;
    public Map<String, Role> roles;

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Project) {
            final Project other = (Project) obj;
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
        return "Project{" + "code=" + code + '}';
    }
}
