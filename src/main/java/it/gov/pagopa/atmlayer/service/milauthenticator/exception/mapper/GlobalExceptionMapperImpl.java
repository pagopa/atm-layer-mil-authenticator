package it.gov.pagopa.atmlayer.service.milauthenticator.exception.mapper;

import io.quarkus.arc.properties.IfBuildProperty;
import io.smallrye.mutiny.CompositeException;
import it.gov.pagopa.atmlayer.service.milauthenticator.exception.AtmLayerException;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ErrorResponse;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

@IfBuildProperty(name = "mapper.enabled", stringValue = "true", enableIfMissing = true)
@Singleton
@Slf4j
public class GlobalExceptionMapperImpl {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionMapperImpl.class);


    @ServerExceptionMapper
    public RestResponse<ErrorResponse> genericExceptionMapper(AtmLayerException exception) {
        return buildErrorResponse(exception);
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> compositeException(CompositeException exception) {
        return buildErrorResponse(new AtmLayerException(exception));
    }

    @ServerExceptionMapper
    public RestResponse<ErrorResponse> genericExceptionMapper(Exception exception) {
        String message = "Generic Error";
        logger.error("Generic error found: ", exception);
        return buildErrorResponse(message);
    }

    private RestResponse<ErrorResponse> buildErrorResponse(AtmLayerException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(e.getType())
                .status(e.getStatusCode())
                .detail(e.getMessage())
                .instance(e.getErrorCode())
                .build();
        return RestResponse.status(Response.Status.fromStatusCode(e.getStatusCode()), errorResponse);
    }

    private RestResponse<ErrorResponse> buildErrorResponse(String message) {
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, ErrorResponse.builder()
                .type(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .detail(message)
                .build());
    }


}
