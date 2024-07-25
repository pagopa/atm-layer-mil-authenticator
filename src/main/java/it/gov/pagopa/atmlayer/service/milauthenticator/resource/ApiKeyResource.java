package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ApiKeyDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.UsagePlanDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.impl.ApiKeyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Path("/api-gateway")
public class ApiKeyResource {

    @Inject
    ApiKeyService apiKeyService;

    @GET
    @Path("/api-key/retrieve/{clientName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ApiKeyDTO> getApiKey(@PathParam("clientName") String clientName) {
        return apiKeyService.getApiKey(clientName);
    }

    @POST
    @Path("/api-key/generate")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ApiKeyDTO> generateApiKey(@HeaderParam("clientName") String clientName) {
        return apiKeyService.createApiKey(clientName);
    }

    @POST
    @Path("/create-usage-plan")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UsagePlanDTO> createUsagePlan(@HeaderParam("planName") String planName, @HeaderParam("apiKeyId") String apiKeyId) {
        return apiKeyService.createUsagePlan(planName, apiKeyId);
    }

}
