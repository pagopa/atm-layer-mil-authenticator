package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.impl.CognitoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.jboss.resteasy.reactive.RestResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolClientType;

@ApplicationScoped
@Path("/cognito")
public class CognitoResource {

    @Inject
    CognitoService cognitoService;

    @GET
    @Path("/client-credentials")
    public Uni<RestResponse<UserPoolClientType>> getClientCredentials() {
        return cognitoService.getClientCredentials()
                .map(RestResponse::ok);
    }
}


