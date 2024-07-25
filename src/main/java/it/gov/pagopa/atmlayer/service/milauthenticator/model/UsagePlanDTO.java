package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.apigateway.model.QuotaSettings;
import software.amazon.awssdk.services.apigateway.model.ThrottleSettings;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsagePlanDTO {
    private String id;
    private String name;
    private String description;
    private ThrottleSettings throttle;
    private QuotaSettings quota;
}
