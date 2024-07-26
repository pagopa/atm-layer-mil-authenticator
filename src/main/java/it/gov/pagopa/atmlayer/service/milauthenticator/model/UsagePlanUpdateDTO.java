package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.apigateway.model.QuotaPeriodType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsagePlanUpdateDTO {
    private Double rateLimit;
    private Integer burstLimit;
    private Integer quotaLimit;
    private QuotaPeriodType quotaPeriod;
    private String name;
}
