package ru.skycelot.plicanterra.metamodel;

import java.util.Map;

/**
 *
 */
public class Status {

    public Long id;
    public Template template;
    public String code;
    public String name;
    public String description;
    public Boolean initial;
    public Map<String, Transition> outcomes;

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Status) {
            final Status other = (Status) obj;
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
        return "Status{" + "code=" + code + ", template=" + template + '}';
    }
}
