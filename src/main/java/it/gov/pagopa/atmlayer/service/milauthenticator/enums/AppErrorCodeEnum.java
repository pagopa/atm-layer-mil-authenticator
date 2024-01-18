package it.gov.pagopa.atmlayer.service.milauthenticator.enums;

import lombok.Getter;

import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorType.*;

/**
 * Enumeration for application error codes and messages
 */
@Getter
public enum AppErrorCodeEnum {

    ATML_MIL_AUTH_500("ATML_MIL_AUTH_500", "An unexpected error has occurred, see logs for more info", GENERIC),
    TOKEN_NOT_FOUND("ATML_MIL_AUTH_00001", "Token not found in cache", ID_NOT_FOUND),
    MIL_UNAVAILABLE("ATML_MIL_AUTH_00002", "MIL not available", MIL_COMMUNICATION),
    REDIS_UNAVAILABLE("ATML_MIL_AUTH_00003", "Redis not available", REDIS_COMMUNICATION);

    private final String errorCode;
    private final String errorMessage;
    private final AppErrorType type;

    AppErrorCodeEnum(String errorCode, String errorMessage, AppErrorType type) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.type = type;
    }
}
