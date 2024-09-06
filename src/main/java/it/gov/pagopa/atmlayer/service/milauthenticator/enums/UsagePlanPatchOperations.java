package it.gov.pagopa.atmlayer.service.milauthenticator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import software.amazon.awssdk.services.apigateway.model.Op;

@Getter
@AllArgsConstructor
public enum UsagePlanPatchOperations {

    RATE_LIMIT(Op.REPLACE,"/throttle/rateLimit"),
    BURST_LIMIT(Op.REPLACE, "/throttle/burstLimit"),
    QUOTA_LIMIT(Op.REPLACE, "/quota/limit"),
    QUOTA_PERIOD(Op.REPLACE, "/quota/period"),
    NAME(Op.REPLACE, "/name"),
    DESCRIPTION(Op.REPLACE, "/description");

    private final Op op;
    private final String path;

}
