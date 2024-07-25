package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.ApiKey;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyResponse;
import software.amazon.awssdk.services.apigateway.model.GetApiKeysRequest;

@ApplicationScoped
public class ApiKeyService {

    private final ApiGatewayClient apiGatewayClient;

    public ApiKeyService() {
        this.apiGatewayClient = ApiGatewayClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.EU_SOUTH_1)
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .build();
    }

    public Uni<String> createApiKey(String clientName) {
        return Uni.createFrom().item(() -> {
            CreateApiKeyRequest request = CreateApiKeyRequest.builder()
                    .name(clientName + "-api-key")
                    .enabled(true)
                    .build();

            CreateApiKeyResponse response = apiGatewayClient.createApiKey(request);
            return response.id(); // La chiave API viene restituita come ID
        });
    }

    public Uni<String> getApiKey(String clientName) {
        return Uni.createFrom().item(() -> {
            // Cerca le chiavi API con il nome specificato
            GetApiKeysRequest request = GetApiKeysRequest.builder()
                    .nameQuery(clientName)
                    .limit(1) // Limita a un risultato
                    .build();

            return apiGatewayClient.getApiKeysPaginator(request).items().stream()
                    .findFirst()
                    .map(ApiKey::id)
                    .orElse(null);
        });
    }
}
