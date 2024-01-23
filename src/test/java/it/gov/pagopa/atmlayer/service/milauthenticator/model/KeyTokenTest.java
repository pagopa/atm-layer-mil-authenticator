package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class KeyTokenTest {

    @Test
    void testToString() {
        KeyToken keyToken = new KeyToken("terminal123", "acquirer456", "channel789", "transactionABC");
        String result = keyToken.toString();
        assertEquals("acquirer456_channel789_terminal123_transactionABC", result);
    }

    @Test
    void testToStringWithNullValues() {
        KeyToken keyToken = new KeyToken(null, "acquirer456", "channel789", null);
        String result = keyToken.toString();
        assertEquals("acquirer456_channel789", result);
    }

    @Test
    void testToStringWithEmptyValues() {
        KeyToken keyToken = new KeyToken("", "acquirer456", "channel789", "");
        String result = keyToken.toString();
        assertEquals("acquirer456_channel789", result);
    }

    @Test
    void testEqualsAndHashCode() {
        KeyToken keyToken1 = new KeyToken("terminal123", "acquirer456", "channel789", "transactionABC");
        KeyToken keyToken2 = new KeyToken("terminal123", "acquirer456", "channel789", "transactionABC");
        assertEquals(keyToken1, keyToken2);
        assertEquals(keyToken1.hashCode(), keyToken2.hashCode());
    }
}
