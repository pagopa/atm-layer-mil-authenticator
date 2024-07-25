package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.impl.ApiKeyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Path("/api-key")
public class ApiKeyResource {

    @Inject
    ApiKeyService apiKeyService;

    @GET
    @Path("/retrieve/{clientName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<String> getApiKey(@PathParam("clientName") String clientName) {
        return apiKeyService.getApiKey(clientName);
    }

    @POST
    @Path("/generate")
    public Uni<String> generateApiKey(@HeaderParam("clientName") String clientName) {
        return apiKeyService.createApiKey(clientName);
    }

}
