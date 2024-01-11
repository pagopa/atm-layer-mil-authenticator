package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import io.vertx.core.MultiMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MilRequest {
    private MultiMap headers;
    private MultiMap body;
}
