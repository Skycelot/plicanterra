package ru.skycelot.plicanterra.metamodel;

/**
 *
 */
public class User {

    public Long id;
    public String login;
    public String fullName;
    public String password;
    public Profile profile;

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof User) {
            final User other = (User) obj;
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
        return "User{" + "login=" + login + '}';
    }
}
