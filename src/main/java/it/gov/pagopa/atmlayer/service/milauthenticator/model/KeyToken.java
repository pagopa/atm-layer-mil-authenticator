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

//    private String requestId;

    private String terminalId;

    private String acquirerId;

    private String channel;

//    private String transactionId;

//    private String fiscalCode;

    @Override
    public String toString() {
        return this.acquirerId.concat("_").concat(channel).concat("_").concat(terminalId).concat("_");
        //.concat(transactionId).concat("_")
        //.fiscalCode.concat("_")
        //.concat(requestId)
    }
}