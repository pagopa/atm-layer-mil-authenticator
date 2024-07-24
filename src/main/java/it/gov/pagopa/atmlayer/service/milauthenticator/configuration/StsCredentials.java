/*
package it.gov.pagopa.atmlayer.service.milauthenticator.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;

@ApplicationScoped
@Slf4j
public class StsCredentials {

    public static AwsSessionCredentials getClientCredentials() {
        StsClient stsClient = StsClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.EU_SOUTH_1)
                .build();

        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .roleArn("arn:aws:iam::043745846978:role/pagopa-dev-atm-layer-mil-authenticator-serviceaccount-role")
                .roleSessionName("session-name")
                .build();

        AssumeRoleResponse assumeRoleResponse = stsClient.assumeRole(assumeRoleRequest);

        log.info("AssumeRoleRequest: {}", assumeRoleRequest);

        AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(
                assumeRoleResponse.credentials().accessKeyId(),
                assumeRoleResponse.credentials().secretAccessKey(),
                assumeRoleResponse.credentials().sessionToken());

        log.info("Session Credentials: {}", sessionCredentials);
        log.info("AssumeRoleResponse: {}", assumeRoleResponse);


        return sessionCredentials;


    }

}
*/
