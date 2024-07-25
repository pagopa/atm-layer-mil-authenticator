package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ClientCredentialsDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.impl.CognitoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

    @DELETE
    @Path("/client-credentials/{clientId}")
    public Uni<Response> deleteClient(@PathParam("clientId") String clientId) {
        return cognitoService.deleteClient(clientId)
                .onItem().transform(deleted -> {
                    if (deleted) {
                        return Response.noContent().build();
                    } else {
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity("Client with ID " + clientId + " not found or could not be deleted.")
                                .build();
                    }
                });
    }

}


