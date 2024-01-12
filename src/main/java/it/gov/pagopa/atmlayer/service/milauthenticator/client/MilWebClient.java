package it.gov.pagopa.atmlayer.service.milauthenticator.client;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("")
@RegisterRestClient(configKey = "mil-token-api")
public interface MilWebClient {

    @POST
    @Path("/mil-auth/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getTokenFromMil(@HeaderParam("Content_Type") String contentType,
                                         @HeaderParam("RequestId") String requestId,
                                         @HeaderParam("AcquirerId") String acquirerId,
                                         @HeaderParam("Channel") String channel,
                                         @HeaderParam("TerminalId") String terminalId,
                                         @HeaderParam("FiscalCode") String fiscalCode,
                                         String body);
}
