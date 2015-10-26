package ru.skycelot.plicanterra.metamodel;

/**
 *
 */
public class Role {

    public Long id;
    public String code;
    public String name;
    public String description;
    public Project project;

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Role) {
            final Role other = (Role) obj;
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
        return "Role{" + "code=" + code + ", project=" + project + '}';
    }
}
