package it.gov.pagopa.atmlayer.service.milauthenticator.enums;

import lombok.Getter;

@Getter
public enum AppErrorType {
    GENERIC,
    VALIDATION,
    INTERNAL,
    ID_NOT_FOUND,
    INVALID_ARGUMENT
}