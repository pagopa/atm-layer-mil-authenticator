package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ErrorResponse;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ValidationErrorResponse;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
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
    @Operation(summary = "Restituisce il token presente nella cache", description = "Esegue la GET nella cache Redis e restituisce il token trovato tramite i valori di input")
    @APIResponse(responseCode = "200", description = "Operazione eseguita con successo. Il processo è terminato.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDTO.class)))
    @APIResponse(responseCode = "400", description = "Uno o più valori di input non valorizzati correttamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Token non trovato nella cache con i valori di input inseriti", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Redis non raggiungibile.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Uni<TokenDTO> getToken(@NotEmpty @HeaderParam("AcquirerId") String acquirerId,
                                  @NotEmpty @HeaderParam("Channel") String channel,
                                  @NotEmpty @HeaderParam("TerminalId") String terminalId,
                                  @NotEmpty @HeaderParam("TransactionId") String transactionId) {
        return this.tokenService.getToken(AuthParameters.builder()
                .acquirerId(acquirerId)
                .channel(channel)
                .terminalId(terminalId)
                .transactionId(transactionId)
                .build());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Restituisce il token creato e lo salva nella cache", description = "Chiama il MIL, CREATE token nella cache Redis e restituisce il token creato")
    @APIResponse(responseCode = "200", description = "Operazione eseguita con successo. Il processo è terminato.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDTO.class)))
    @APIResponse(responseCode = "400", description = "Uno o più valori di input non valorizzati correttamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Servizi esterni (cache, generazione token) non raggiungibili.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Uni<TokenDTO> createToken(@NotEmpty @HeaderParam("AcquirerId") String acquirerId,
                                     @NotEmpty @NotEmpty @HeaderParam("Channel") String channel,
                                     @NotEmpty @HeaderParam("TerminalId") String terminalId,
                                     @HeaderParam("FiscalCode") String fiscalCode,
                                     @NotEmpty @HeaderParam("TransactionId") String transactionId) {
        return this.tokenService.generateToken(AuthParameters.builder()
                .acquirerId(acquirerId)
                .channel(channel)
                .terminalId(terminalId)
                .fiscalCode(fiscalCode)
                .transactionId(transactionId)
                .build());
    }


    @DELETE
    @Operation(summary = "Cancella il token salvato nella cache", description = "Esegue la DELETE nella cache Redis")
    @APIResponse(responseCode = "204", description = "Operazione eseguita con successo. Il processo è terminato.")
    @APIResponse(responseCode = "400", description = "Uno o più valori di input non valorizzati correttamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Token non presente in cache.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Redis non raggiungibile.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Uni<Void> deleteToken(@NotEmpty @HeaderParam("AcquirerId") String acquirerId,
                                 @NotEmpty @HeaderParam("Channel") String channel,
                                 @NotEmpty @HeaderParam("TerminalId") String terminalId,
                                 @NotEmpty @HeaderParam("TransactionId") String transactionId) {
        return this.tokenService.deleteToken(AuthParameters.builder()
                .acquirerId(acquirerId)
                .channel(channel)
                .terminalId(terminalId)
                .transactionId(transactionId)
                .build());
    }
}
