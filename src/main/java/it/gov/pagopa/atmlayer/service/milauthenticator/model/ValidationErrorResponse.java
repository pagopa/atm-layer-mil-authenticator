package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

/**
 * Model class for the error response
 */
@Getter
@Jacksonized
@JsonPropertyOrder({"type", "errorCode", "status", "message", "errors"})
@RegisterForReflection
@SuperBuilder
public class ValidationErrorResponse extends ErrorResponse {
    @Schema(type = SchemaType.ARRAY, maxItems = 100)
    private List<String> errors;
}
