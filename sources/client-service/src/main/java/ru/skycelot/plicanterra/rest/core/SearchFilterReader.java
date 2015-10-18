package ru.skycelot.plicanterra.rest.core;

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
import ru.skycelot.plicanterra.crud.transfer.search.SearchFilter;

/**
 *
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class SearchFilterReader implements MessageBodyReader<SearchFilter> {

    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt) {
        return type == SearchFilter.class && mt.isCompatible(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public SearchFilter readFrom(Class<SearchFilter> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {
        return parseFromJson(in);
    }

    private SearchFilter parseFromJson(InputStream json) {
        try {
            ObjectReader reader = new ObjectMapper().reader();
            SearchFilter result = reader.readValue(json);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
