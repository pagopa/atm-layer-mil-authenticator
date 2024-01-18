package it.gov.pagopa.atmlayer.service.milauthenticator.service.impl;

import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.Request;
import it.gov.pagopa.atmlayer.service.milauthenticator.client.MilWebClient;
import it.gov.pagopa.atmlayer.service.milauthenticator.configuration.RequestHeaders;
import it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorCodeEnum;
import it.gov.pagopa.atmlayer.service.milauthenticator.enums.RequiredVariables;
import it.gov.pagopa.atmlayer.service.milauthenticator.exception.AtmLayerException;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.KeyToken;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.Token;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.properties.AuthProperties;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.TokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Inject
    @RestClient
    MilWebClient milWebClient;

    @Inject
    AuthProperties authProperties;

    @Inject
    Redis redis;

    @Override
    public Uni<TokenDTO> getToken(AuthParameters authParameters) {
        KeyToken keyToken = getKeyToken(authParameters);
        return Uni.createFrom().completionStage(redis.send(Request.cmd(Command.create("GET")).arg(keyToken.toString())).toCompletionStage())
                .onItem().transform(response -> {
                    TokenDTO tokenDTO = new TokenDTO();
                    if (response != null) {
                        String token = response.toString();
                        if (token != null && !token.isEmpty()) {
                            log.info("Token found in cache");
                            tokenDTO.setAccessToken(token);
                            return tokenDTO;
                        }
                    }
                    log.info("Token not found in cache");
                    throw new AtmLayerException("Token not found in cache", Response.Status.NOT_FOUND, AppErrorCodeEnum.TOKEN_NOT_FOUND);
                }).onFailure().recoverWithUni(failure -> {
                    log.error(failure.getMessage());
                    return Uni.createFrom().failure(new AtmLayerException("Redis request failed, service unavailable", Response.Status.INTERNAL_SERVER_ERROR, AppErrorCodeEnum.REDIS_UNAVAILABLE));
                });
    }

    private static KeyToken getKeyToken(AuthParameters authParameters) {
        KeyToken keyToken = new KeyToken();
        keyToken.setChannel(authParameters.getChannel());
        keyToken.setAcquirerId(authParameters.getAcquirerId());
        keyToken.setTerminalId(authParameters.getTerminalId());
        keyToken.setTransactionId(authParameters.getTransactionId());
        return keyToken;
    }

//    @Override
//    public Uni<TokenDTO> generateToken(AuthParameters authParameters) {
//        KeyToken keyToken = getKeyToken(authParameters);
//        log.info("mil request starting");
//        RequestHeaders headers = prepareAuthHeaders(authParameters);
//        String body = prepareAuthBody();
//        log.info("request ready");
//        Uni<Token> tokenUni = milWebClient.getTokenFromMil(headers.getContentType(), headers.getRequestId(), headers.getAcquirerId(), headers.getChannel(), headers.getTerminalId(), headers.getFiscalCode(), body);
//
//        return tokenUni.onFailure()
//                .recoverWithUni(failure -> {
//                    log.error(failure.getMessage());
//                    return Uni.createFrom().failure(new AtmLayerException("MIL unavailable", Response.Status.INTERNAL_SERVER_ERROR, AppErrorCodeEnum.MIL_UNAVAILABLE));
//                })
//                .onItem()
//                .transformToUni(token -> {
//                    log.info("redis connection starting");
//                    redis.send(Request.cmd(Command.create("SET"))
//                            .arg(keyToken.toString())
//                            .arg(token.getAccessToken())
//                            .arg("EX")
//                            .arg(token.getExpiresIn()))
//                            .recover(throwable -> {
//                                log.error("Redis unavailable", throwable);
//                                return Future.failedFuture(new AtmLayerException("Redis unavailable", Response.Status.INTERNAL_SERVER_ERROR, AppErrorCodeEnum.REDIS_UNAVAILABLE));
//                            })
//                            .onComplete(response -> log.info("redis request completed"))
//                            .transform(responseAsyncResult -> {
//                                responseAsyncResult.map(response -> {
//                                    TokenDTO tokenDTO = new TokenDTO();
//                                    tokenDTO.setAccessToken(token.getAccessToken());
//                                    return Uni.createFrom().item(tokenDTO);
//                                });
//                                return null;
//                            });
//
//                    return null;
//                });
//    }

    @Override
    public Uni<TokenDTO> generateToken(AuthParameters authParameters) {
        KeyToken keyToken = getKeyToken(authParameters);
        log.info("mil request starting");
        RequestHeaders headers = prepareAuthHeaders(authParameters);
        String body = prepareAuthBody();
        log.info("request ready");

        Uni<Token> tokenUni = milWebClient.getTokenFromMil(headers.getContentType(), headers.getRequestId(), headers.getAcquirerId(), headers.getChannel(), headers.getTerminalId(), headers.getFiscalCode(), body);

        return tokenUni
                .onFailure()
                .recoverWithUni(failure -> {
                    log.error(failure.getMessage());
                    return Uni.createFrom().failure(new AtmLayerException("MIL unavailable", Response.Status.INTERNAL_SERVER_ERROR, AppErrorCodeEnum.MIL_UNAVAILABLE));
                })
                .onItem()
                .transformToUni(token -> {
                    log.info("redis connection starting");
                    return Uni.createFrom().completionStage(
                            redis.send(Request.cmd(Command.create("SET"))
                                    .arg(keyToken.toString())
                                    .arg(token.getAccessToken())
                                    .arg("EX")
                                    .arg(token.getExpiresIn())
                            ).toCompletionStage()
                    ).onItem().transformToUni(ignore -> {
                        TokenDTO tokenDTO = new TokenDTO();
                        tokenDTO.setAccessToken(token.getAccessToken());
                        return Uni.createFrom().item(tokenDTO);
                    }).onFailure().recoverWithUni(failure -> {
                        log.error(failure.getMessage());
                        return Uni.createFrom().failure(new AtmLayerException("Redis request failed, service unavailable", Response.Status.INTERNAL_SERVER_ERROR, AppErrorCodeEnum.REDIS_UNAVAILABLE));
                    });



                });
    }



    private String prepareAuthBody() {
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put(RequiredVariables.CLIENT_ID.getValue(), authProperties.getClientId());
        bodyParams.put(RequiredVariables.CLIENT_SECRET.getValue(), authProperties.getClientSecret());
        bodyParams.put(RequiredVariables.GRANT_TYPE.getValue(), authProperties.getGrantType());
        String body = bodyParams.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "="
                        + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return body;
    }

    private static RequestHeaders prepareAuthHeaders(AuthParameters authParameters) {
        RequestHeaders headers = new RequestHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setRequestId(UUID.randomUUID().toString());
        headers.setAcquirerId(authParameters.getAcquirerId());
        headers.setChannel(authParameters.getChannel());
        headers.setTerminalId(authParameters.getTerminalId());
        headers.setFiscalCode(authParameters.getFiscalCode());
        return headers;
    }
}
