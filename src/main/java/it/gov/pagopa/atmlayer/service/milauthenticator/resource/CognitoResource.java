package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ClientCredentialsDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.impl.CognitoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolClientType;

@ApplicationScoped
@Path("/cognito")
public class CognitoResource {

    @Inject
    CognitoService cognitoService;

    @GET
    @Path("/client-credentials")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ClientCredentialsDTO> getClientCredentials() {
        return cognitoService.getClientCredentials();
    }

    @POST
    @Path("/client-credentials")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ClientCredentialsDTO> generateClient(@HeaderParam("clientName") @NotBlank String clientName) {
        return cognitoService.generateClient(clientName);
    }

}


