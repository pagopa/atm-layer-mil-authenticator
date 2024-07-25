package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ApiKeyDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.UsagePlanDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.UsagePlanUpdateDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.impl.ApiKeyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

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
    public Uni<UsagePlanDTO> createUsagePlan(@HeaderParam("planName") String planName, @HeaderParam("apiKeyId") String apiKeyId, @HeaderParam("limit") int limit, @HeaderParam("period") String period, @HeaderParam("burstLimit") int burstLimit, @HeaderParam("rateLimit") double rateLimit) {
        return apiKeyService.createUsagePlan(planName, apiKeyId, limit, period, burstLimit, rateLimit);
    }

    @GET
    @Path("/usage-plan/{usagePlanId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UsagePlanDTO> getUsagePlan(@PathParam("usagePlanId") String usagePlanId) {
        return apiKeyService.getUsagePlan(usagePlanId);
    }

    @DELETE
    @Path("/usage-plan/{usagePlanId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Void> deleteUsagePlan(@PathParam("usagePlanId") String usagePlanId) {
        return apiKeyService.deleteUsagePlan(usagePlanId);
    }

    @PUT
    @Path("/usage-plan/{usagePlanId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UsagePlanDTO> updateUsagePlan(@PathParam("usagePlanId") String usagePlanId, @RequestBody UsagePlanUpdateDTO usagePlanUpdateDTO) {
        return apiKeyService.updateUsagePlan(usagePlanId, usagePlanUpdateDTO);
    }

}
