package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ApiKeyDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.UsagePlanDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.UsagePlanUpdateDTO;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.UsagePlanPatchOperations.*;

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
                .httpClient(ApacheHttpClient.builder().build())
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

    public Uni<Void> deleteApiKey(String apiKeyId) {
        return Uni.createFrom().item(() -> {
                    DeleteApiKeyRequest request = DeleteApiKeyRequest.builder()
                            .apiKey(apiKeyId)
                            .build();
                    apiGatewayClient.deleteApiKey(request);
                    return null;
        }).onFailure().invoke(th -> log.error("Failed to delete usage plan with id: {}", apiKeyId, th)).replaceWithVoid();
    }

    public Uni<UsagePlanDTO> createUsagePlan(String planName, String apiKeyId, int limit, QuotaPeriodType period, int burstLimit, double rateLimit) {
        return Uni.createFrom().item(() -> {
            CreateUsagePlanRequest usagePlanRequest = CreateUsagePlanRequest.builder()
                    .name(planName)
                    .description("Usage plan for " + planName)
                    .quota(q -> q.limit(limit).period(period.toString()))
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
        log.info("-------- preparing patchOperations");
        // Build patch operations to update the usage plan
        List<PatchOperation> patchOperations = new ArrayList<>();
        if (updateDTO.getName() != null) {
            patchOperations.add(PatchOperation.builder().op(Op.REPLACE).path("/name").value(updateDTO.getName()).build());
        }
        Optional.ofNullable(updateDTO.getName()).ifPresent(name -> patchOperations.add(PatchOperation.builder().op(NAME.getOp()).path(NAME.getPath()).value(name).build()));
        Optional.of(updateDTO.getQuotaLimit()).ifPresent(quotaLimit -> patchOperations.add(PatchOperation.builder().op(QUOTA_LIMIT.getOp()).path(QUOTA_LIMIT.getPath()).value(String.valueOf(quotaLimit)).build()));
        Optional.of(updateDTO.getQuotaPeriod()).ifPresent(quotaPeriod -> patchOperations.add(PatchOperation.builder().op(QUOTA_PERIOD.getOp()).path(QUOTA_PERIOD.getPath()).value(quotaPeriod.toString()).build()));
        Optional.of(updateDTO.getBurstLimit()).ifPresent(burstLimit -> patchOperations.add(PatchOperation.builder().op(BURST_LIMIT.getOp()).path(BURST_LIMIT.getPath()).value(String.valueOf(burstLimit)).build()));
        Optional.of(updateDTO.getRateLimit()).ifPresent(rateLimit ->patchOperations.add(PatchOperation.builder().op(RATE_LIMIT.getOp()).path(RATE_LIMIT.getPath()).value(String.valueOf(rateLimit)).build()));
        log.info("-------- prepared patchOperations: {}", patchOperations);
        return patchOperations;
    }

    public Uni<Void> deleteUsagePlan(String usagePlanId) {
        return Uni.createFrom().item(() -> {


            /*DeleteUsagePlanKeyRequest usagePlanKeyRequest = DeleteUsagePlanKeyRequest.builder()
                    .usagePlanId(usagePlanId)
                    .keyId(apiKeyId)
                    .build();
            apiGatewayClient.deleteUsagePlanKey(usagePlanKeyRequest);*/


            List<PatchOperation> patchOperations = new ArrayList<>();
            patchOperations.add(PatchOperation.builder().op(Op.REMOVE).path("/apiStages").value(apiGatewayId+":"+apiGatewayStage).build());
            UpdateUsagePlanRequest updateUsagePlanRequest = UpdateUsagePlanRequest.builder()
                    .usagePlanId(usagePlanId)
                    .patchOperations(patchOperations)
                    .build();
            apiGatewayClient.updateUsagePlan(updateUsagePlanRequest);

            DeleteUsagePlanRequest usagePlanRequest = DeleteUsagePlanRequest.builder()
                    .usagePlanId(usagePlanId)
                    .build();

            DeleteUsagePlanResponse usagePlanResponse = apiGatewayClient.deleteUsagePlan(usagePlanRequest);

            log.info("Usage plan: {}", usagePlanResponse);
            return null; // Return null to indicate completion with no value
        }).onFailure().invoke(th -> log.error("Failed to delete usage plan with id: {}", usagePlanId, th)).replaceWithVoid();
    }
}
