package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.exception.AtmLayerException;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ErrorResponse;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorCodeEnum.ATML_MIL_AUTH_500;
import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorCodeEnum.TOKEN_NOT_FOUND;

@ApplicationScoped
@Path("/token")
@Tag(name = "Token", description = "Token Extraction")
@Slf4j
public class TokenResource {

    @Inject
    TokenService tokenService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Restituisce il token presente nella cache", description = "Esegue la GET nella cache Redis e restituisce il token trovato tramite i valori di input")
    @APIResponse(responseCode = "200", description = "Operazione eseguita con successo. Il processo è terminato.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDTO.class)))
    @APIResponse(responseCode = "404", description = "Token non trovato nella cache con i valori di input inseriti", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Errore generico.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Uni<Response> getToken(@HeaderParam("AcquirerId") String acquirerId,
                                  @HeaderParam("Channel") String channel,
                                  @HeaderParam("TerminalId") String terminalId,
                                  @HeaderParam("TransactionId") String transactionId) {

        return this.tokenService.getToken(
                        AuthParameters.builder()
                                .acquirerId(acquirerId)
                                .channel(channel)
                                .terminalId(terminalId)
                                .transactionId(transactionId)
                                .build())
                .onItem().transform(tokenDTO -> Response.ok(tokenDTO).build())
                .onFailure().recoverWithItem(exception -> {
                    ErrorResponse errorResponse;
                    if(exception instanceof AtmLayerException && ((AtmLayerException) exception).getStatusCode() == Response.Status.NOT_FOUND.getStatusCode()) {
                        errorResponse = ErrorResponse.builder()
                                .type(TOKEN_NOT_FOUND.getType().name())
                                .status(404)
                                .detail(TOKEN_NOT_FOUND.getErrorMessage())
                                .instance(TOKEN_NOT_FOUND.name())
                                .build();
                    }else {
                        errorResponse = ErrorResponse.builder()
                                .type(ATML_MIL_AUTH_500.getType().name())
                                .status(500)
                                .detail(ATML_MIL_AUTH_500.getErrorMessage())
                                .instance(ATML_MIL_AUTH_500.name())
                                .build();
                    }
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(errorResponse)
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                });
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Restituisce il token creato e lo salva nella cache", description = "Chiama il MIL, CREATE token nella cache Redis e restituisce il token creato")
    @APIResponse(responseCode = "200", description = "Operazione eseguita con successo. Il processo è terminato.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDTO.class)))
    @APIResponse(responseCode = "500", description = "Errore generico.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Uni<TokenDTO> createToken(@HeaderParam("AcquirerId") String acquirerId,
                                     @HeaderParam("Channel") String channel,
                                     @HeaderParam("TerminalId") String terminalId,
                                     @HeaderParam("FiscalCode") String fiscalCode,
                                     @HeaderParam("TransactionId") String transactionId) {

        return this.tokenService.generateToken(AuthParameters.builder()
                .acquirerId(acquirerId)
                .channel(channel)
                .terminalId(terminalId)
                .fiscalCode(fiscalCode)
                .transactionId(transactionId)
                .build());
    }
}
