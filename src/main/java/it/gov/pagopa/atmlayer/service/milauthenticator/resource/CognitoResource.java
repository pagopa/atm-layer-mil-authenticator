package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.impl.CognitoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolClientType;

@ApplicationScoped
@Path("/cognito")
@Produces(MediaType.APPLICATION_JSON)
public class CognitoResource {

    @Inject
    CognitoService cognitoService;

    @GET
    @Path("/client-credentials")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UserPoolClientType> getClientCredentials() {
        return cognitoService.getClientCredentials();
    }
}

