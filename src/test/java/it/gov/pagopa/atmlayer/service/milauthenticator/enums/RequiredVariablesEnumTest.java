package it.gov.pagopa.atmlayer.service.milauthenticator.enums;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class RequiredVariablesEnumTest {

    @Test
    void testEnumValues() {
        assertEquals("AcquirerId", RequiredVariables.ACQUIRER_ID.getValue());
        assertEquals("BranchId", RequiredVariables.BRANCH_ID.getValue());
        assertEquals("TerminalId", RequiredVariables.TERMINAL_ID.getValue());
        assertEquals("FiscalCode", RequiredVariables.FISCAL_CODE.getValue());
        assertEquals("TransactionId", RequiredVariables.TRANSACTION_ID.getValue());
        assertEquals("client_id", RequiredVariables.CLIENT_ID.getValue());
        assertEquals("client_secret", RequiredVariables.CLIENT_SECRET.getValue());
        assertEquals("grant_type", RequiredVariables.GRANT_TYPE.getValue());
        assertEquals("content_type", RequiredVariables.CONTENT_TYPE.getValue());
    }
}
