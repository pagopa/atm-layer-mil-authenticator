package it.gov.pagopa.atmlayer.service.milauthenticator.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@ApplicationScoped
public class AwsClientConf {

        private CognitoIdentityProviderClient cognitoClient;

        @Inject
        CognitoConfig config;

    @PostConstruct
    void init() {
        /*this.cognitoClient = CognitoIdentityProviderClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.of(config.region()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();*/

        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .httpClientBuilder(ApacheHttpClient.builder())
                .region(Region.EU_SOUTH_1)
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .build();
    }

        public CognitoIdentityProviderClient getCognitoClient() {
            return cognitoClient;
        }
    }
