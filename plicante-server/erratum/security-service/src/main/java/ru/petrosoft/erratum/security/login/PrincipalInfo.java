package ru.petrosoft.erratum.security.login;

import java.util.List;
import ru.petrosoft.erratum.security.core.ErratumPrincipal;
import ru.petrosoft.erratum.security.core.RolePrincipal;

/**
 *
 */
public class PrincipalInfo {

    public ErratumPrincipal principal;
    public List<RolePrincipal> roles;
}
