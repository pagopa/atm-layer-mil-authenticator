package it.gov.pagopa.atmlayer.service.milauthenticator.enums;

import lombok.Getter;

@Getter
public enum AppErrorType {
    GENERIC,
    VALIDATION,
    INTERNAL,
    KEY_TOKEN_NOT_FOUND,
    INVALID_ARGUMENT,
    MIL_COMMUNICATION,
    REDIS_COMMUNICATION
}