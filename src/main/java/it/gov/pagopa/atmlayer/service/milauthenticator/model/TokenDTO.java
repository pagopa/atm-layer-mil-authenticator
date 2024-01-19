package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDTO {
    @JsonProperty("access_token")
    private String accessToken;
}
