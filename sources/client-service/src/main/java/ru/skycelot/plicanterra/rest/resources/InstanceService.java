package ru.skycelot.plicanterra.rest.resources;

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
import ru.skycelot.plicanterra.crud.service.CrudService;
import ru.skycelot.plicanterra.crud.transfer.Instance;
import ru.skycelot.plicanterra.crud.transfer.search.SearchFilter;
import ru.skycelot.plicanterra.crud.transfer.search.SearchResult;
import ru.skycelot.plicanterra.rest.core.ErratumRestException;

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

    @POST
    public Long createInstance(Instance instance) {
        try {
            return lookupCrudService().createInstance(instance);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @PUT
    public void updateInstance(Instance instance) {
        try {
            lookupCrudService().updateInstance(instance);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @Path("{instanceId}")
    @GET
    public Instance findInstance(@PathParam("instanceId") Long instanceId) {
        try {
            return lookupCrudService().findInstance(instanceId);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @Path("{instanceId}")
    @DELETE
    public void removeInstance(@PathParam("instanceId") Long instanceId) {
        try {
            lookupCrudService().removeInstance(instanceId);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @Path("template/{templateId}")
    @GET
    public Instance getSketchInstance(@PathParam("templateId") Long templateId) {
        try {
            return lookupCrudService().getSketchInstance(templateId);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @Path("template/{templateId}")
    @POST
    public Instance createInstance(@PathParam("templateId") Long templateId) {
        try {
            return lookupCrudService().createInstance(templateId);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @Path("${instanceId}/status/${statusId}")
    @PUT
    public void changeInstanceStatus(@PathParam("instanceId") Long instanceId, @PathParam("statusId") Long statusId) {
        try {
            lookupCrudService().changeInstanceStatus(instanceId, statusId);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }

    @Path("search")
    @PUT
    public SearchResult searchInstances(SearchFilter filter) {
        try {
            return lookupCrudService().searchInstances(filter);
        } catch (Exception e) {
            throw new ErratumRestException(e);
        }
    }
}
