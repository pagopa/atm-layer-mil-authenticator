package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyToken {

    private String terminalId;

    private String acquirerId;

    private String branchId;

    private String transactionId;

    private String fiscalCode;

    @Override
    public String toString() {
        return this.fiscalCode.concat("_").concat(acquirerId).concat("_").concat(branchId).concat("_").concat(terminalId).concat("_").concat(transactionId);
    }
}