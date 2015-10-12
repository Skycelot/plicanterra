package ru.petrosoft.erratum.rest.resources;

import java.util.Collections;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import ru.petrosoft.erratum.crud.service.CrudService;
import ru.petrosoft.erratum.crud.transfer.Instance;
import ru.petrosoft.erratum.crud.transfer.Template;
import ru.petrosoft.erratum.crud.transfer.search.SearchFilter;
import ru.petrosoft.erratum.crud.transfer.search.SearchResult;
import ru.petrosoft.erratum.rest.core.ErratumRestException;

/**
 *
 */
@Path("/instance")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InstanceService {

    private CrudService lookupCrudService() {
        try {
            return InitialContext.doLookup("java:global/erratum/CrudService");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("{templateId}")
    public Instance getSkeleton(@PathParam("templateId") Long templateId) {
        try {
            return lookupCrudService().getSketchInstance(templateId);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @POST
    public SearchResult searchInstances(SearchFilter filter) {
        try {
            SearchResult result = new SearchResult();
            Instance instance = new Instance();
            instance.template = new Template();
            instance.template.id = filter.templateId;
            instance.id = ((Number) filter.searchElements.get(0).values.get(0)).longValue();
            instance.version = 7L;
            result.number = 1;
            result.instances = Collections.singletonList(instance);
            return result;
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @PUT
    public Long saveInstance(Instance instance) {
        try {
            return instance.id += 1;
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @DELETE
    @Path("{instanceId}")
    public void removeInstance(@PathParam("instanceId") Long instanceId) {
        try {
            instanceId += 1;
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }
}
