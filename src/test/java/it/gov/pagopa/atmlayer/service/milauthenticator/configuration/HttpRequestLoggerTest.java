package it.gov.pagopa.atmlayer.service.milauthenticator.configuration;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@QuarkusTest
class HttpRequestLoggerTest {

    @Mock
    ContainerRequestContext requestContext;

    @Mock
    UriInfo uriInfo;

    @InjectMocks
    HttpRequestLogger httpRequestLogger;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testLogRequest() {
//        when(requestContext.getHeaderString("TransactionId")).thenReturn("testTransactionId");
//        when(requestContext.getUriInfo()).thenReturn(uriInfo);
//        when(uriInfo.getAbsolutePath()).thenReturn(URI.create("/test/uri"));
//        when(requestContext.getMethod()).thenReturn("GET");
//
//        MultivaluedMap<String, String> someHeaders = new MultivaluedHashMap<>();
//        someHeaders.add("Header1", "Value1");
//        someHeaders.add("Header2", "Value2");
//        when(requestContext.getHeaders()).thenReturn(someHeaders);
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outputStream));
//
//        httpRequestLogger.logRequest(requestContext);
//
//        String logOutput = outputStream.toString();
//        assertEquals(logOutput, ("====================================request started with transactionId= testTransactionId\n" +
//                "URI         : /test/uri\n" +
//                "Method      : GET\n" +
//                "Headers     : {Header1=[Value1], Header2=[Value2]}"));
//    }
}
