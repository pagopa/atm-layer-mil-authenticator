package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorCodeEnum;
import it.gov.pagopa.atmlayer.service.milauthenticator.exception.AtmLayerException;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ErrorResponse;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
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
    @APIResponse(responseCode = "400", description = "Uno o più valori di input non valorizzati correttamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Token non trovato nella cache con i valori di input inseriti", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Redis non raggiungibile.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Uni<TokenDTO> getToken(@Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("AcquirerId") String acquirerId,
                                  @Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("Channel") String channel,
                                  @Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("TerminalId") String terminalId,
                                  @Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("TransactionId") String transactionId) {

        AuthParameters authParameters = new AuthParameters();
        try {
            authParameters = AuthParameters.builder()
                    .acquirerId(acquirerId)
                    .channel(channel)
                    .terminalId(terminalId)
                    .transactionId(transactionId)
                    .build();
        } catch (Exception e) {
            throw new AtmLayerException("Invalid request parameters", Response.Status.BAD_REQUEST, AppErrorCodeEnum.MALFORMED_REQUEST);
        }
        return this.tokenService.getToken(authParameters);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Restituisce il token creato e lo salva nella cache", description = "Chiama il MIL, CREATE token nella cache Redis e restituisce il token creato")
    @APIResponse(responseCode = "200", description = "Operazione eseguita con successo. Il processo è terminato.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDTO.class)))
    @APIResponse(responseCode = "400", description = "Uno o più valori di input non valorizzati correttamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Servizi esterni (cache, generazione token) non raggiungibili.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Uni<TokenDTO> createToken(@Parameter(in = ParameterIn.HEADER, required = true)@NotNull @HeaderParam("AcquirerId") String acquirerId,
                                     @Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("Channel") String channel,
                                     @Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("TerminalId") String terminalId,
                                     @HeaderParam("FiscalCode") String fiscalCode,
                                     @Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("TransactionId") String transactionId) {
        AuthParameters authParameters = new AuthParameters();
        try {
            authParameters = AuthParameters.builder()
                    .acquirerId(acquirerId)
                    .channel(channel)
                    .terminalId(terminalId)
                    .fiscalCode(fiscalCode)
                    .transactionId(transactionId)
                    .build();
        } catch (Exception e) {
            throw new AtmLayerException("Invalid request parameters", Response.Status.BAD_REQUEST, AppErrorCodeEnum.MALFORMED_REQUEST);
        }
        return this.tokenService.generateToken(authParameters);
    }


    @DELETE
    @Operation(summary = "Cancella il token salvato nella cache", description = "Esegue la DELETE nella cache Redis")
    @APIResponse(responseCode = "204", description = "Operazione eseguita con successo. Il processo è terminato.")
    @APIResponse(responseCode = "400", description = "Uno o più valori di input non valorizzati correttamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "404", description = "Token non presente in cache.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @APIResponse(responseCode = "500", description = "Redis non raggiungibile.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public Uni<Void> deleteToken(@Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("AcquirerId") String acquirerId,
                                 @Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("Channel") String channel,
                                 @Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("TerminalId") String terminalId,
                                 @Parameter(in = ParameterIn.HEADER, required = true)@HeaderParam("TransactionId") String transactionId) {
        AuthParameters authParameters = new AuthParameters();
        try {
            authParameters = AuthParameters.builder()
                    .acquirerId(acquirerId)
                    .channel(channel)
                    .terminalId(terminalId)
                    .transactionId(transactionId)
                    .build();
        } catch (Exception e) {
            throw new AtmLayerException("Invalid request parameters", Response.Status.BAD_REQUEST, AppErrorCodeEnum.MALFORMED_REQUEST);
        }
        return this.tokenService.deleteToken(authParameters);
    }
}
