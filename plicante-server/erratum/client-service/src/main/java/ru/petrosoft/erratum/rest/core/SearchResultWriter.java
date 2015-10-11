package ru.petrosoft.erratum.rest.core;

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
import ru.petrosoft.erratum.crud.transfer.search.SearchResult;

/**
 *
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class SearchResultWriter implements MessageBodyWriter<SearchResult> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == SearchResult.class && mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public long getSize(SearchResult t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return convertToJson(t).length();
    }

    @Override
    public void writeTo(SearchResult t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        entityStream.write(convertToJson(t).getBytes("UTF8"));
    }

    private String convertToJson(SearchResult instance) {
        try {
            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String result = writer.writeValueAsString(instance);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
