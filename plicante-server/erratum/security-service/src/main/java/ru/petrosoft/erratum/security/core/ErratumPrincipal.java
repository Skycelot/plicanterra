package ru.petrosoft.erratum.security.core;

import java.security.Principal;
import java.util.Set;

/**
 *
 */
public class ErratumPrincipal implements Principal {

    private final String login;
    private String fullName;
    private Set<RolePrincipal> roles;

    public ErratumPrincipal(String login) {
        this.login = login;
    }

    @Override
    public String getName() {
        return login;
    }

    public String getFullName() {
        return fullName;
}

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<RolePrincipal> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolePrincipal> roles) {
        this.roles = roles;
    }
}
