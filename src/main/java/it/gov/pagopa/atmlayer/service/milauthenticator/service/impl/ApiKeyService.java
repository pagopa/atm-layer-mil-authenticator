package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ApiKeyDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.UsagePlanDTO;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.*;

@ApplicationScoped
@Slf4j
public class ApiKeyService {

    private final ApiGatewayClient apiGatewayClient;

    @ConfigProperty(name = "api-gateway.id")
    String apiGatewayId;

    @ConfigProperty(name = "app.environment")
    String apiGatewayStage;

    public ApiKeyService() {
        this.apiGatewayClient = ApiGatewayClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.EU_SOUTH_1)
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .build();
    }

    public Uni<ApiKeyDTO> createApiKey(String clientName) {
        return Uni.createFrom().item(() -> {
            CreateApiKeyRequest request = CreateApiKeyRequest.builder()
                    .name(clientName + "-api-key")
                    .enabled(true)
                    .build();

            CreateApiKeyResponse response = apiGatewayClient.createApiKey(request);
            return new ApiKeyDTO(response.id(), response.value(), response.name()); // La chiave API viene restituita come ID
        });
    }

    public Uni<ApiKeyDTO> getApiKey(String clientName) {
        return Uni.createFrom().item(() -> {
            // Cerca le chiavi API con il nome specificato
            GetApiKeysRequest request = GetApiKeysRequest.builder()
                    .nameQuery(clientName)
                    .includeValues(true)
                    .limit(1) // Limita a un risultato
                    .build();

            return apiGatewayClient.getApiKeysPaginator(request).items().stream()
                    .findFirst()
                    .map(apiKey -> {
                        log.info("Api key: {}", apiKey);
                       return new ApiKeyDTO(apiKey.id(), apiKey.value(), apiKey.name());
                    })
                    .orElse(null);
        });
    }

    public Uni<UsagePlanDTO> createUsagePlan(String planName, String apiKeyId, int limit, String period, int burstLimit, double rateLimit ) {
        return Uni.createFrom().item(() -> {
            CreateUsagePlanRequest usagePlanRequest = CreateUsagePlanRequest.builder()
                    .name(planName)
                    .description("Usage plan for " + planName)
                    .quota(q -> q.limit(limit).period(period))
                    .throttle(t -> t.burstLimit(burstLimit).rateLimit(rateLimit))
                    .apiStages(ApiStage.builder().apiId(apiGatewayId).stage(apiGatewayStage).build())
                    .build();

            CreateUsagePlanResponse usagePlanResponse = apiGatewayClient.createUsagePlan(usagePlanRequest);
            UsagePlanDTO usagePlan = new UsagePlanDTO(usagePlanResponse.id(), usagePlanResponse.name(), usagePlanRequest.description());

            // Associa la chiave API al Usage Plan
            CreateUsagePlanKeyRequest usagePlanKeyRequest = CreateUsagePlanKeyRequest.builder()
                    .usagePlanId(usagePlanResponse.id())
                    .keyId(apiKeyId)
                    .keyType("API_KEY")
                    .build();
            apiGatewayClient.createUsagePlanKey(usagePlanKeyRequest);

            log.info("Usage plan: {}", usagePlan);
            
            return usagePlan;
        });
    }

    public Uni<UsagePlanDTO> getUsagePlan(String usagePlanId) {
        return Uni.createFrom().item(() -> {
            GetUsagePlanRequest usagePlanRequest = GetUsagePlanRequest.builder()
                    .usagePlanId(usagePlanId)
                    .build();

            GetUsagePlanResponse usagePlanResponse = apiGatewayClient.getUsagePlan(usagePlanRequest);
            UsagePlanDTO usagePlan = new UsagePlanDTO(usagePlanResponse.id(), usagePlanResponse.name(), usagePlanResponse.description());

            log.info("Usage plan: {}", usagePlan);

            return usagePlan;
        });
    }
}
