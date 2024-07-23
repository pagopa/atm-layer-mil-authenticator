package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.configuration.CognitoConfig;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DescribeUserPoolClientRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DescribeUserPoolClientResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolClientType;

@ApplicationScoped
@Slf4j
public class CognitoService {
    private CognitoIdentityProviderClient cognitoClient;
    private ObjectMapper objectMapper;
    @Inject
    CognitoConfig config;

    @PostConstruct
    void init() {
        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.of(config.region())
                )
//                .credentialsProvider(StaticCredentialsProvider.create(
//                        AwsBasicCredentials.create(config.accessKeyId, config.secretAccessKey)
//                ))
                .build();
    }

    public Uni<String> getClientCredentials() {
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
                return objectMapper.writeValueAsString(client);
            } catch (JsonProcessingException e) {
                log.error("mapping exception");
                throw new RuntimeException(e);
            }
        });
    }
}

