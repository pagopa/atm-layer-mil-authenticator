package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.configuration.CognitoConfig;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ClientCredentialsDTO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;


@ApplicationScoped
@Slf4j
public class CognitoService {
    private CognitoIdentityProviderClient cognitoClient;

    @Inject
    ObjectMapper objectMapper;
    @Inject
    CognitoConfig config;

    @PostConstruct
    void init() {
        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.of(config.region())
                )
                .build();
    }

    public Uni<ClientCredentialsDTO> getClientCredentials() {
        return Uni.createFrom().item(() -> {
            DescribeUserPoolClientRequest request = DescribeUserPoolClientRequest.builder()
                    .userPoolId("eu-south-1_sEZF9PqAf")
                    .clientId("6bn45fharnm6gj4a2ipifj5nbt")
                    .build();
            UserPoolClientType client = null;
            try {
                DescribeUserPoolClientResponse response = cognitoClient.describeUserPoolClient(request);
                client= response.userPoolClient();
                log.info("Client value: {}", client);
            } catch (Exception e) {
                log.error("ERROR with getClientCredentials: {}", e.getMessage());
            }
            try {
                ClientCredentialsDTO clientCredentialsDTO = new ClientCredentialsDTO();
                clientCredentialsDTO.setClientId(client != null ? client.clientId() : "");
                clientCredentialsDTO.setClientSecret(client != null ? client.clientSecret() : "");
                clientCredentialsDTO.setClientName(client != null ? client.clientName() : "");
                return clientCredentialsDTO;
            } catch (Exception e) {
                log.error("mapping exception");
                throw new RuntimeException(e);
            }
        });
    }

    public Uni<ClientCredentialsDTO> generateClient(String clientName) {
        return Uni.createFrom().item(() -> {
            CreateUserPoolClientRequest request = CreateUserPoolClientRequest.builder()
                    .userPoolId("eu-south-1_sEZF9PqAf")
                    .clientName(clientName)
                    .build();
            UserPoolClientType client = null;
            try {
                CreateUserPoolClientResponse response = cognitoClient.createUserPoolClient(request);
                client= response.userPoolClient();
                log.info("Client value: {}", client);
            } catch (Exception e) {
                log.error("ERROR with getClientCredentials: {}", e.getMessage());
            }
            try {
                ClientCredentialsDTO clientCredentialsDTO = new ClientCredentialsDTO();
                clientCredentialsDTO.setClientId(client != null ? client.clientId() : "");
                clientCredentialsDTO.setClientSecret(client != null ? client.clientSecret() : "");
                clientCredentialsDTO.setClientName(client != null ? client.clientName() : "");
                return clientCredentialsDTO;
            } catch (Exception e) {
                log.error("mapping exception");
                throw new RuntimeException(e);
            }
        });
    }

}

