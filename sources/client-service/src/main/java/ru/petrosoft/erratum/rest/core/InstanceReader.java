package ru.petrosoft.erratum.rest.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import ru.petrosoft.erratum.crud.transfer.Instance;

/**
 *
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class InstanceReader implements MessageBodyReader<Instance> {

    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return type == Instance.class && mt.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public Instance readFrom(Class<Instance> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {
        return parseFromJson(in);
    }

    private Instance parseFromJson(InputStream json) {
        try {
            ObjectReader reader = new ObjectMapper().reader();
            Instance result = reader.readValue(json);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
