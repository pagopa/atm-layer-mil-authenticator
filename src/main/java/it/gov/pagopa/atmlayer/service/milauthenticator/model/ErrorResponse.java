package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Model class for the error response
 */
@Getter
@SuperBuilder
@Jacksonized
@JsonPropertyOrder({"type", "status", "detail", "instance"})
@RegisterForReflection
public class ErrorResponse {

    @Size(max=255)
    private String type;

    @Schema(example = "500", minimum = "1", maximum = "999")
    private int status;

    @Schema(example = "An unexpected error has occurred. Please contact support.", maxLength = 1000)
    private String message;

    @Schema(example = "ATML_MIL_AUTH-500", maxLength = 255)
    private String errorCode;
}