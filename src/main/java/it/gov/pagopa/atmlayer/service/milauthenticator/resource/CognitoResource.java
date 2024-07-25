package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ClientCredentialsDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.impl.CognitoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/cognito")
public class CognitoResource {

    @Inject
    CognitoService cognitoService;

    @GET
    @Path("/client-credentials/{clientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ClientCredentialsDTO> getClientCredentials(@PathParam("clientId") String clientId) {
        return cognitoService.getClientCredentials(clientId);
    }

    @POST
    @Path("/client-credentials")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ClientCredentialsDTO> generateClient(@HeaderParam("clientName") @NotBlank String clientName) {
        return cognitoService.generateClient(clientName);
    }

}


