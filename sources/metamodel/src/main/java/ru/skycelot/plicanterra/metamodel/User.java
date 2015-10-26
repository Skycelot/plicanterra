package ru.skycelot.plicanterra.metamodel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import ru.skycelot.plicanterra.util.orm.Filter;

/**
 *
 */
@Table(name = "principal")
public class User {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "login")
    private String login;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "user")
    @Filter(field = "project_id", contextual = true)
    private Profile profile;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

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
