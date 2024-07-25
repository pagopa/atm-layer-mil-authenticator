package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ApiKeyDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.UsagePlanDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.UsagePlanUpdateDTO;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.ApiStage;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyResponse;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanRequest;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanResponse;
import software.amazon.awssdk.services.apigateway.model.GetApiKeysRequest;
import software.amazon.awssdk.services.apigateway.model.GetUsagePlanRequest;
import software.amazon.awssdk.services.apigateway.model.GetUsagePlanResponse;
import software.amazon.awssdk.services.apigateway.model.Op;
import software.amazon.awssdk.services.apigateway.model.PatchOperation;
import software.amazon.awssdk.services.apigateway.model.UpdateUsagePlanRequest;
import software.amazon.awssdk.services.apigateway.model.UpdateUsagePlanResponse;

import java.util.ArrayList;
import java.util.List;

import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.UsagePlanPatchOperations.BURST_LIMIT;
import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.UsagePlanPatchOperations.QUOTA_LIMIT;
import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.UsagePlanPatchOperations.QUOTA_PERIOD;
import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.UsagePlanPatchOperations.RATE_LIMIT;

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

    public Uni<UsagePlanDTO> createUsagePlan(String planName, String apiKeyId, int limit, String period, int burstLimit, double rateLimit) {
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

    public Uni<UsagePlanDTO> updateUsagePlan(String usagePlanId, UsagePlanUpdateDTO usagePlanUpdateDTO) {
        return Uni.createFrom().item(() -> {
            UpdateUsagePlanRequest updateUsagePlanRequest = UpdateUsagePlanRequest.builder()
                    .usagePlanId(usagePlanId)
                    .patchOperations(buildPatchOperation(usagePlanUpdateDTO))
                    .build();
            UpdateUsagePlanResponse updatedPlan = apiGatewayClient.updateUsagePlan(updateUsagePlanRequest);
            UsagePlanDTO usagePlan = new UsagePlanDTO(updatedPlan.id(), updatedPlan.name(), updatedPlan.name());
            log.info("Updated usage plan: {}", usagePlan);
            return usagePlan;
        }).onFailure().invoke(th -> log.error("Failed to update usage plan with id: {}", usagePlanId, th));
    }

    public List<PatchOperation> buildPatchOperation(UsagePlanUpdateDTO updateDTO) {
        // Build patch operations to update the usage plan
        List<PatchOperation> patchOperations = new ArrayList<>();
        if (updateDTO.getName() != null) {
            patchOperations.add(PatchOperation.builder().op(Op.REPLACE).path("/name").value(updateDTO.getName()).build());
        }
        patchOperations.add(PatchOperation.builder().op(QUOTA_LIMIT.getOp()).path(QUOTA_LIMIT.getPath()).value(String.valueOf(updateDTO.getQuotaLimit())).build());
        patchOperations.add(PatchOperation.builder().op(QUOTA_PERIOD.getOp()).path(QUOTA_PERIOD.getPath()).value(updateDTO.getQuotaPeriod()).build());
        patchOperations.add(PatchOperation.builder().op(BURST_LIMIT.getOp()).path(BURST_LIMIT.getPath()).value(String.valueOf(updateDTO.getBurstLimit())).build());
        patchOperations.add(PatchOperation.builder().op(RATE_LIMIT.getOp()).path(RATE_LIMIT.getPath()).value(String.valueOf(updateDTO.getRateLimit())).build());
        return patchOperations;
    }
}
