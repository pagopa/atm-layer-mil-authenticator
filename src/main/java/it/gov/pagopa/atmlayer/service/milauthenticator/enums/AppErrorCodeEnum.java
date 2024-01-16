package it.gov.pagopa.atmlayer.service.milauthenticator.enums;

import lombok.Getter;

import static it.gov.pagopa.atmlayer.service.milauthenticator.enums.AppErrorType.*;

/**
 * Enumeration for application error codes and messages
 */
@Getter
public enum AppErrorCodeEnum {

    ATML_MIL_AUTH_500("ATML_MIL_AUTH_500", "An unexpected error has occurred, see logs for more info", GENERIC),
    TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", "Token not found in cache", ID_NOT_FOUND);

    private final String errorCode;
    private final String errorMessage;
    private final AppErrorType type;

    AppErrorCodeEnum(String errorCode, String errorMessage, AppErrorType type) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.type = type;
    }
}
