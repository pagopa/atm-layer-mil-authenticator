package it.gov.pagopa.atmlayer.service.milauthenticator.exception.mapper;

import io.quarkus.arc.properties.IfBuildProperty;
import io.smallrye.mutiny.CompositeException;
import it.gov.pagopa.atmlayer.service.milauthenticator.exception.AtmLayerException;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ErrorResponse;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.ValidationErrorResponse;
import it.gov.pagopa.atmlayer.service.milauthenticator.utils.ConstraintViolationMappingUtils;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
    @Inject
    ConstraintViolationMappingUtils constraintViolationMappingUtils;

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionMapperImpl.class);

    @ServerExceptionMapper
    public RestResponse<ValidationErrorResponse> constraintViolationExceptionMapper(ConstraintViolationException exception) {
        String message = "Validation Error on Payload";
        logger.error("Validation Error on Payload: ", exception);
        return buildErrorResponse(exception.getConstraintViolations(), message);
    }



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
        return buildErrorResponse(message, exception);
    }

    private RestResponse<ErrorResponse> buildErrorResponse(AtmLayerException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .type(e.getType())
                .status(e.getStatusCode())
                .message(e.getMessage())
                .errorCode(e.getErrorCode())
                .build();
        return RestResponse.status(Response.Status.fromStatusCode(e.getStatusCode()), errorResponse);
    }

    private RestResponse<ErrorResponse> buildErrorResponse(String message, Exception e) {
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, ErrorResponse.builder()
                .type(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .message(message + " " + e)
                .build());
    }

    private RestResponse<ErrorResponse> buildErrorResponse(String message) {
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, ErrorResponse.builder()
                .type(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .message(message)
                .build());
    }

    private RestResponse<ValidationErrorResponse> buildErrorResponse(Set<ConstraintViolation<?>> errors, String message) {
        List<String> errorMessages = constraintViolationMappingUtils.extractErrorMessages(errors);
        ValidationErrorResponse payload = ValidationErrorResponse.builder()
                .type(Response.Status.BAD_REQUEST.getReasonPhrase())
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .message(message)
                .errors(errorMessages)
                .build();
        return RestResponse.status(Response.Status.BAD_REQUEST, payload);
    }


}
