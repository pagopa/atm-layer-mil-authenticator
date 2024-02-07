package it.gov.pagopa.atmlayer.service.milauthenticator.properties;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Data;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Data
@ApplicationScoped
public class AuthProperties {
    @Inject
    @ConfigProperty(name = "auth.client-id")
    String clientId;
    @Inject
    @ConfigProperty(name = "auth.client-secret")
    String clientSecret;
    @Inject
    @ConfigProperty(name = "auth.grant-type")
    String grantType;
}
