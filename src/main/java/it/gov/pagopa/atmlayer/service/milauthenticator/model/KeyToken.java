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



    @Override
    public String toString() {
        return this.acquirerId.concat("_").concat(channel).concat("_").concat(terminalId);
        //.concat(transactionId).concat("_")
        //.concat(requestId)
    }
}