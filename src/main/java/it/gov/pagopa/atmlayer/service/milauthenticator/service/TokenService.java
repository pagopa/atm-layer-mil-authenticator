package it.gov.pagopa.atmlayer.service.milauthenticator.service;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;

public interface TokenService {

    Uni<TokenDTO> getToken(AuthParameters authParameters);

    Uni<TokenDTO> generateToken(AuthParameters authParameters);
}
