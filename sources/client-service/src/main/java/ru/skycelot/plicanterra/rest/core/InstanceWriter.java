package ru.skycelot.plicanterra.rest.core;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.skycelot.plicanterra.crud.transfer.Instance;

/**
 *
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class InstanceWriter implements MessageBodyWriter<Instance> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Instance.class && mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public long getSize(Instance t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return convertToJson(t).length();
    }

    @Override
    public void writeTo(Instance t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        entityStream.write(convertToJson(t).getBytes("UTF8"));
    }

    private String convertToJson(Instance instance) {
        try {
            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String result = writer.writeValueAsString(instance);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
