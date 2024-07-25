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
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import software.amazon.awssdk.services.apigateway.model.QuotaPeriodType;

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

    @DELETE
    @Path("/api-key/delete/{apiKeyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Void> deleteApiKey(@PathParam("apiKeyId") String apiKeyId) {
        return apiKeyService.deleteApiKey(apiKeyId);
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
    public Uni<UsagePlanDTO> createUsagePlan(@HeaderParam("planName") String planName, @HeaderParam("apiKeyId") String apiKeyId, @HeaderParam("limit") int limit, @HeaderParam("period") QuotaPeriodType period, @HeaderParam("burstLimit") int burstLimit, @HeaderParam("rateLimit") double rateLimit) {
        return apiKeyService.createUsagePlan(planName, apiKeyId, limit, period, burstLimit, rateLimit);
    }

    @GET
    @Path("/usage-plan/{usagePlanId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UsagePlanDTO> getUsagePlan(@PathParam("usagePlanId") String usagePlanId) {
        return apiKeyService.getUsagePlan(usagePlanId);
    }

    @DELETE
    @Path("/usage-plan/{usagePlanId}/{apiKeyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Void> deleteUsagePlan(@PathParam("usagePlanId") String usagePlanId, @PathParam("apiKeyId") String apiKeyId) {
        return apiKeyService.deleteUsagePlan(usagePlanId, apiKeyId);
    }

    @PUT
    @Path("/usage-plan/{usagePlanId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UsagePlanDTO> updateUsagePlan(@PathParam("usagePlanId") String usagePlanId, @RequestBody UsagePlanUpdateDTO usagePlanUpdateDTO) {
        return apiKeyService.updateUsagePlan(usagePlanId, usagePlanUpdateDTO);
    }

}
