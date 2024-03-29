package it.gov.pagopa.atmlayer.service.milauthenticator.enums;

import lombok.Getter;

import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorType.GENERIC;
import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorType.INVALID_ARGUMENT;
import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorType.KEY_TOKEN_NOT_FOUND;
import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorType.MIL_COMMUNICATION;
import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorType.REDIS_COMMUNICATION;

/**
 * Enumeration for application error codes and messages
 */
@Getter
public enum AppErrorCodeEnum {

    ATML_MIL_AUTH_500("ATML_MIL_AUTH_500", "An unexpected error has occurred, see logs for more info", GENERIC),
    TOKEN_NOT_FOUND("ATML_MIL_AUTH_00001", "Token not found in cache", KEY_TOKEN_NOT_FOUND),
    MIL_UNAVAILABLE("ATML_MIL_AUTH_00002", "MIL not available", MIL_COMMUNICATION),
    REDIS_UNAVAILABLE("ATML_MIL_AUTH_00003", "Redis not available", REDIS_COMMUNICATION),
    MALFORMED_REQUEST("ATML_MIL_AUTH_00004", "Check your request param", INVALID_ARGUMENT);

    private final String errorCode;
    private final String errorMessage;
    private final AppErrorType type;

    AppErrorCodeEnum(String errorCode, String errorMessage, AppErrorType type) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.type = type;
    }
}
