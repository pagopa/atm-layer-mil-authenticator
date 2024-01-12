package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthParameters {
    private String requestId;
    private String acquirerId;
    private String channel;
    private String terminalId;
    private String fiscalCode;
    private String transactionId;
}
