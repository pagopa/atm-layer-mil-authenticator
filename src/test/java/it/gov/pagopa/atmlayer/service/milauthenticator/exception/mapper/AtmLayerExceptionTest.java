package it.gov.pagopa.atmlayer.service.milauthenticator.exception.mapper;

import io.quarkus.test.junit.QuarkusTest;
import it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorCodeEnum;
import it.gov.pagopa.atmlayer.service.milauthenticator.exception.AtmLayerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class AtmLayerExceptionTest {

    @Test
    void testExceptionWithThrowable() {
        Throwable throwable = new RuntimeException("Test error");
        AtmLayerException exception = AtmLayerException.builder().error(throwable).build();

        assertNotNull(exception);
        assertEquals("Test error", exception.getMessage());
        assertEquals(AppErrorCodeEnum.ATML_MIL_AUTH_500.getType().name(), exception.getType());
        assertEquals(500, exception.getStatusCode());
        assertEquals(AppErrorCodeEnum.ATML_MIL_AUTH_500.getErrorCode(), exception.getErrorCode());
        assertEquals(throwable, exception.getCause());
    }
}