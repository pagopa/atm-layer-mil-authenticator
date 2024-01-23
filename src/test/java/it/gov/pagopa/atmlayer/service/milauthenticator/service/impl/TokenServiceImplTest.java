package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.Request;
import it.gov.pagopa.atmlayer.service.milauthenticator.configuration.RequestHeaders;
import it.gov.pagopa.atmlayer.service.milauthenticator.enums.RequiredVariables;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.KeyToken;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.Token;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.properties.AuthProperties;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class TokenServiceImplTest {

//    @Mock
//    MilWebClient milWebClient;

    @Inject
    TokenServiceImpl tokenService;

    @Mock
    Redis redis;

    @Mock
    Uni<Response> uniResponseMock;

    String testChannel = "testChannel";
    String testAcquirer = "testAcquirer";
    String testTerminal = "testTerminal";
    String testTransaction = "testTransaction";
    String clientId = "testClientId";
    String grantType = "testGrantType";
    String clientSecret = "testClientSecret";
    String testFiscalCode = "testFiscalCode";
    String testRequest = "testRequest";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getKeyToken() {
        KeyToken keyToken = new KeyToken();
        keyToken.setChannel(testChannel);
        keyToken.setAcquirerId(testAcquirer);
        keyToken.setTerminalId(testTerminal);
        keyToken.setTransactionId(testTransaction);
        assertEquals(new KeyToken(testTerminal, testAcquirer, testChannel, testTransaction), keyToken);
    }

    @Test
    void prepareAuthBody() {
        Map<String, String> bodyParams = new HashMap<>();
        AuthProperties authProperties = new AuthProperties();
        authProperties.setClientId(clientId);
        authProperties.setGrantType(grantType);
        authProperties.setClientSecret(clientSecret);
        bodyParams.put(RequiredVariables.CLIENT_ID.getValue(), authProperties.getClientId());
        bodyParams.put(RequiredVariables.CLIENT_SECRET.getValue(), authProperties.getClientSecret());
        bodyParams.put(RequiredVariables.GRANT_TYPE.getValue(), authProperties.getGrantType());
        String body = bodyParams.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "="
                        + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        assertEquals("grant_type=testGrantType&client_secret=testClientSecret&client_id=testClientId", body);
    }

    @Test
    void prepareAuthHeaders() {
        RequestHeaders headers = new RequestHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setRequestId(testRequest);
        headers.setAcquirerId(testAcquirer);
        headers.setChannel(testChannel);
        headers.setTerminalId(testTerminal);
        headers.setFiscalCode(testFiscalCode);
        assertEquals(new RequestHeaders(MediaType.APPLICATION_FORM_URLENCODED, testRequest, testAcquirer, testChannel, testTerminal, testFiscalCode), headers);
    }
}
