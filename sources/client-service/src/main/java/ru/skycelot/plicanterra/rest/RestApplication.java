package ru.skycelot.plicanterra.rest;

import ru.skycelot.plicanterra.rest.core.InstanceWriter;
import ru.skycelot.plicanterra.rest.core.ErratumRestExceptionHandler;
import ru.skycelot.plicanterra.rest.core.ErratumRestException;
import ru.skycelot.plicanterra.rest.resources.InstanceService;
import ru.skycelot.plicanterra.rest.resources.TestService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import ru.skycelot.plicanterra.rest.resources.SessionService;

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
        result.add(SessionService.class);
        return result;
    }

    @Override
    public Set<Object> getSingletons() {
        return Collections.emptySet();
    }
}
