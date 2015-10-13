package ru.petrosoft.erratum.security.core;

import java.security.Principal;

/**
 *
 */
public class RolePrincipal implements Principal {

    private String name;

    public RolePrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
