package ru.petrosoft.erratum.rest.core;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 */
public class ErratumRestExceptionHandler implements ExceptionMapper<ErratumRestException> {

    @Override
    public Response toResponse(ErratumRestException exception) {
        Throwable cause = extractInitialException(exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(cause.toString()).build();
    }

    private Throwable extractInitialException(Throwable external) {
        Throwable result = external;
        while (result.getCause() != null) {
            result = result.getCause();
        }
        return result;
    }
}
