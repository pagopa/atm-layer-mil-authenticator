package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class TokenDTO {
    @JsonProperty("access_token")
    @Schema(format = "byte", maxLength = 100000)
    private String accessToken;
}
