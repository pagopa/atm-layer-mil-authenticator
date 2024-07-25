package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsagePlanUpdateDTO {
    private double rateLimit;
    private int burstLimit;
    private int quotaLimit;
    private String quotaPeriod;
    private String name;
}
