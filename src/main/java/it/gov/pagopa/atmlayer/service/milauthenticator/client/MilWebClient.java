package it.gov.pagopa.atmlayer.service.milauthenticator.client;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.Token;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("")
@RegisterRestClient(configKey = "mil-token-api")
public interface MilWebClient {

    @POST
    @Path("/mil-auth/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Token> getTokenFromMil(@NotNull @HeaderParam("Content_Type") String contentType,
                               @NotNull @HeaderParam("RequestId") String requestId,
                               @NotNull @HeaderParam("AcquirerId") String acquirerId,
                               @NotNull @HeaderParam("Channel") String channel,
                               @NotNull @HeaderParam("TerminalId") String terminalId,
                               @NotNull String body);
}
