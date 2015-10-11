package ru.petrosoft.erratum.rest;

import ru.petrosoft.erratum.rest.core.InstanceWriter;
import ru.petrosoft.erratum.rest.core.ErratumRestExceptionHandler;
import ru.petrosoft.erratum.rest.core.ErratumRestException;
import ru.petrosoft.erratum.rest.resources.InstanceService;
import ru.petrosoft.erratum.rest.resources.TestService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 */
@ApplicationPath("/")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> result = new HashSet<>();
        result.add(InstanceService.class);
        result.add(ErratumRestException.class);
        result.add(ErratumRestExceptionHandler.class);
        result.add(InstanceWriter.class);
        result.add(TestService.class);
        return result;
    }

    @Override
    public Set<Object> getSingletons() {
        return Collections.emptySet();
    }
}
