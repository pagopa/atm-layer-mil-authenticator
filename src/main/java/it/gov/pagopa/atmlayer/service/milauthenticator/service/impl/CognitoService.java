package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.configuration.AwsClientConf;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ClientCredentialsDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;


@ApplicationScoped
@Slf4j
public class CognitoService {
    @Inject
    AwsClientConf awsClientConf;

    @Inject
    ObjectMapper objectMapper;

    @ConfigProperty(name = "cognito.user-pool.id")
    String userPoolId;

    public Uni<ClientCredentialsDTO> getClientCredentials(String clientId) {
        return Uni.createFrom().item(() -> {
            DescribeUserPoolClientRequest request = DescribeUserPoolClientRequest.builder()
                    .userPoolId(userPoolId)
                    .clientId(clientId)
                    .build();
            UserPoolClientType client = null;
            try {
                DescribeUserPoolClientResponse response = awsClientConf.getCognitoClient().describeUserPoolClient(request);
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
                    .generateSecret(true)
                    .build();
            UserPoolClientType client = null;
            try {
                CreateUserPoolClientResponse response = awsClientConf.getCognitoClient().createUserPoolClient(request);
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

    public Uni<Boolean> deleteClient(String clientId) {
        return Uni.createFrom().item(() -> {
            try {
                DeleteUserPoolClientRequest request = DeleteUserPoolClientRequest.builder()
                        .userPoolId(userPoolId)
                        .clientId(clientId)
                        .build();
                awsClientConf.getCognitoClient().deleteUserPoolClient(request);
                log.info("Client with ID {} deleted successfully", clientId);
                return true;
            } catch (Exception e) {
                log.error("ERROR with deleteClient: {}", e.getMessage());
                return false;
            }
        });
    }

}

