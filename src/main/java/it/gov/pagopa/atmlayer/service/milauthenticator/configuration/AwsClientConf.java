package it.gov.pagopa.atmlayer.service.milauthenticator.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@ApplicationScoped
public class AwsClientConf {

        private CognitoIdentityProviderClient cognitoClient;

        @Inject
        CognitoConfig config;

        @PostConstruct
        public void init() {
            this.cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(Region.of(config.region())) // Imposta la regione
                    .build();
        }

        public CognitoIdentityProviderClient getCognitoClient() {
            return cognitoClient;
        }
    }
