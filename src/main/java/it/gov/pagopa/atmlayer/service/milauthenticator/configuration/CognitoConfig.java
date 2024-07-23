package it.gov.pagopa.atmlayer.service.milauthenticator.configuration;


import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "aws-cognito")
@StaticInitSafe
public interface CognitoConfig {
    String region();
}

