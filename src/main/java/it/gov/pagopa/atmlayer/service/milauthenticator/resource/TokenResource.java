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
                                .title(TOKEN_NOT_FOUND.getErrorCode())
                                .status(404)
                                .detail(TOKEN_NOT_FOUND.getErrorMessage())
                                .instance(TOKEN_NOT_FOUND.name())
                                .build();
                    }else {
                        errorResponse = ErrorResponse.builder()
                                .type(ATML_MIL_AUTH_500.getType().name())
                                .title(ATML_MIL_AUTH_500.getErrorCode())
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
    public Uni<Response> createToken(@HeaderParam("AcquirerId") String acquirerId,
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
                .build()).onItem().transform(tokenDTO -> Response.ok(tokenDTO).build())
                .onFailure().recoverWithItem(exception -> {

                    ErrorResponse errorResponse = ErrorResponse.builder()
                                .type(ATML_MIL_AUTH_500.getType().name())
                                .title(ATML_MIL_AUTH_500.getErrorCode())
                                .status(500)
                                .detail(ATML_MIL_AUTH_500.getErrorMessage())
                                .instance(ATML_MIL_AUTH_500.name())
                                .build();
                    
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(errorResponse)
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                });
    }
}
