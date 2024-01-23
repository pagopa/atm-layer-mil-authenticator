package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.redis.client.Response;
import io.vertx.redis.client.Redis;
import it.gov.pagopa.atmlayer.service.milauthenticator.client.MilWebClient;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.KeyToken;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.Token;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.properties.AuthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class TokenServiceImplTest {

    @Mock
    private MilWebClient milWebClient;

    @Mock
    private AuthProperties authProperties;

    @Mock
    private Redis redis;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGenerateTokenSuccess() {
//        // Arrange
//        AuthParameters authParameters = new AuthParameters("a", "b", "c", "d", "e", "f");
//
//        Token expectedToken = new Token();
//        expectedToken.setAccessToken("test");
//        when(milWebClient.getTokenFromMil(any(), any(), any(), any(), any(), any(), any())).thenReturn(Uni.createFrom().item(expectedToken));
//
//        // Act
//        Uni<TokenDTO> result = tokenService.generateToken(authParameters);
//
//        // Assert
//        assertNotNull(result);
//        TokenDTO tokenDTO = result.await().indefinitely();
//        assertNotNull(tokenDTO);
//        assertEquals(expectedToken.getAccessToken(), tokenDTO.getAccessToken());
//        // Add more assertions if needed
//    }
}
