package it.gov.pagopa.atmlayer.service.milauthenticator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequiredVariables {
    ACQUIRER_ID("AcquirerId"),
    BRANCH_ID("BranchId"),
    TERMINAL_ID("TerminalId"),
    FISCAL_CODE("FiscalCode"),
    TRANSACTION_ID("TransactionId"),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    GRANT_TYPE("grant_type"),
    CONTENT_TYPE("content_type");

    private final String value;
}
