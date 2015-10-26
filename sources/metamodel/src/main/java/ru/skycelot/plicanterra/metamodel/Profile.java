package ru.skycelot.plicanterra.metamodel;

import java.util.Map;

/**
 *
 */
public class Profile {

    public Long id;
    public User user;
    public Project project;
    public Map<String, Role> roles;

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Profile) {
            final Profile other = (Profile) obj;
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
        return "Profile{" + "user=" + user + ", project=" + project + '}';
    }
}
