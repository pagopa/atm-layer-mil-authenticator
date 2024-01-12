package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@ApplicationScoped
@Path("/token")
@Tag(name = "Token", description = "Token Extraction")
@Slf4j
public class TokenResource {

    @Inject
    TokenService tokenService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<TokenDTO> getToken(@HeaderParam("RequestId") String requestId,
                                  @HeaderParam("AcquirerId") String acquirerId,
                                  @HeaderParam("Channel") String channel,
                                  @HeaderParam("TerminalId") String terminalId,
                                  @HeaderParam("Fiscalcode") String fiscalCode,
                                  @HeaderParam("TransactionId") String transactionId) {

        return this.tokenService.getToken(new AuthParameters(requestId, acquirerId, channel, terminalId, fiscalCode, transactionId));
    }
}
