package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.Request;
import io.vertx.redis.client.Response;
import io.vertx.redis.client.impl.types.SimpleStringType;
import it.gov.pagopa.atmlayer.service.milauthenticator.client.MilWebClient;
import it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorCodeEnum;
import it.gov.pagopa.atmlayer.service.milauthenticator.exception.AtmLayerException;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.Token;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.properties.AuthProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class TokenServiceImplTest {

    @Mock
    MilWebClient milWebClient;

    @Mock
    AuthProperties authProperties;

//    @Mock
//    Redis redis;

    @InjectMocks
    TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetTokenFoundInCache() {
//        AuthParameters authParameters = new AuthParameters("a", "b", "c", "d");
//        Response redisResponse = SimpleStringType.create("token_test");
//        Future<Response> redisResponseFuture = Future.succeededFuture(redisResponse);
//        when(redis.send(any(Request.class))).thenReturn(redisResponseFuture);
//        Uni<TokenDTO> result = tokenService.getToken(authParameters);
//        TokenDTO tokenDTO = result.await().indefinitely();
//        assertNotNull(tokenDTO);
//        assertEquals("token_test", tokenDTO.getAccessToken());
//    }

//    @Test
//    void testRedisUnavailable() {
//        AuthParameters authParameters = new AuthParameters("a", "b", "c", "d");
//        Future<Response> redisResponseFuture = Future.failedFuture(new RuntimeException("Redis not available"));
//        when(redis.send(any(Request.class))).thenReturn(redisResponseFuture);
//        Uni<TokenDTO> result = tokenService.getToken(authParameters);
//        AtmLayerException atmLayerException = assertThrows(AtmLayerException.class, () -> result.await().indefinitely());
//        assertEquals(AppErrorCodeEnum.REDIS_UNAVAILABLE.getErrorCode(), atmLayerException.getErrorCode());
//    }

//    @Test
//    void testGetTokenNotFoundInCache() {
//        AuthParameters authParameters = new AuthParameters("a", "b", "c", "d");
//        Future<Response> response = Future.succeededFuture(null);
//        when(redis.send(any(Request.class))).thenReturn(response);
//        Uni<TokenDTO> result = tokenService.getToken(authParameters);
//        AtmLayerException atmLayerException = assertThrows(AtmLayerException.class, () -> result.await().indefinitely());
//        assertEquals(AppErrorCodeEnum.TOKEN_NOT_FOUND.getErrorCode(), atmLayerException.getErrorCode());
//    }

    @Test
    void testGenerateToken() {
        AuthParameters authParameters = new AuthParameters("a", "b", "c", "d");
        Token mockedToken = new Token();
        mockedToken.setAccessToken("test_token");
        when(milWebClient.getTokenFromMil(any(), any(), any(), any(), any(), any())).thenReturn(Uni.createFrom().item(mockedToken));
//        Response redisResponse = SimpleStringType.create("token_test");
//        Future<Response> redisResponseFuture = Future.succeededFuture(redisResponse);
//        when(redis.send(any(Request.class))).thenReturn(redisResponseFuture);
        when(authProperties.getClientId()).thenReturn("your_client_id");
        when(authProperties.getClientSecret()).thenReturn("your_client_secret");
        when(authProperties.getGrantType()).thenReturn("your_grant_type");
        Uni<TokenDTO> result = tokenService.generateToken(authParameters);
        TokenDTO tokenDTO = result.await().indefinitely();
        assertNotNull(tokenDTO);
        assertEquals(mockedToken.getAccessToken(), tokenDTO.getAccessToken());
    }

    @Test
    void testGenerateTokenMilUnavailable() {
        AuthParameters authParameters = new AuthParameters("a", "b", "c", "d");
        when(authProperties.getClientId()).thenReturn("your_client_id");
        when(authProperties.getClientSecret()).thenReturn("your_client_secret");
        when(authProperties.getGrantType()).thenReturn("your_grant_type");
        when(milWebClient.getTokenFromMil(any(), any(), any(), any(), any(), any()))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("MIL error")));
        AtmLayerException atmLayerException = assertThrows(
                AtmLayerException.class,
                () -> tokenService.generateToken(authParameters).await().indefinitely()
        );
        assertEquals(AppErrorCodeEnum.MIL_UNAVAILABLE.getErrorCode(), atmLayerException.getErrorCode());
    }

//    @Test
//    void testGenerateTokenRedisUnavailable() {
//        AuthParameters authParameters = new AuthParameters("a", "b", "c", "d");
//        when(authProperties.getClientId()).thenReturn("your_client_id");
//        when(authProperties.getClientSecret()).thenReturn("your_client_secret");
//        when(authProperties.getGrantType()).thenReturn("your_grant_type");
//        when(milWebClient.getTokenFromMil(any(), any(), any(), any(), any(), any()))
//                .thenReturn(Uni.createFrom().item(new Token()));
//        Future<Response> redisResponseFuture = Future.failedFuture(new RuntimeException("Token not found in cache"));
//        when(redis.send(any(Request.class))).thenReturn(redisResponseFuture);
//        AtmLayerException atmLayerException = assertThrows(
//                AtmLayerException.class,
//                () -> tokenService.generateToken(authParameters).await().indefinitely()
//        );
//        assertEquals(AppErrorCodeEnum.REDIS_UNAVAILABLE.getErrorCode(), atmLayerException.getErrorCode());
//    }

//    @Test
//    void testDeleteToken() {
//        AuthParameters authParameters = new AuthParameters("a", "b", "c", "d");
//        Response redisResponse = SimpleStringType.create("1");
//        Future<Response> redisResponseFuture = Future.succeededFuture(redisResponse);
//        when(redis.send(any(Request.class))).thenReturn(redisResponseFuture);
//        Uni<Void> result = tokenService.deleteToken(authParameters);
//        result.await().indefinitely();
//        verify(redis).send(any(Request.class));
//    }

//    @Test
//    void testDeleteTokenNotFound() {
//        AuthParameters authParameters = new AuthParameters("a", "b", "c", "d");
//        Response redisResponse = SimpleStringType.create("0");
//        Future<Response> redisResponseFuture = Future.succeededFuture(redisResponse);
//        when(redis.send(any(Request.class))).thenReturn(redisResponseFuture);
//        Uni<Void> result = tokenService.deleteToken(authParameters);
//        AtmLayerException atmLayerException = assertThrows(AtmLayerException.class, () -> result.await().indefinitely());
//        assertEquals(AppErrorCodeEnum.TOKEN_NOT_FOUND.getErrorCode(), atmLayerException.getErrorCode());
//    }

//    @Test
//    void testDeleteTokenRedisUnavailable() {
//        AuthParameters authParameters = new AuthParameters("a", "b", "c", "d");
//        Future<Response> redisResponseFuture = Future.failedFuture(new RuntimeException("Token not found in cache"));
//        when(redis.send(any(Request.class))).thenReturn(redisResponseFuture);
//        AtmLayerException atmLayerException = assertThrows(
//                AtmLayerException.class,
//                () -> tokenService.deleteToken(authParameters).await().indefinitely()
//        );
//        assertEquals(AppErrorCodeEnum.REDIS_UNAVAILABLE.getErrorCode(), atmLayerException.getErrorCode());
//    }
}