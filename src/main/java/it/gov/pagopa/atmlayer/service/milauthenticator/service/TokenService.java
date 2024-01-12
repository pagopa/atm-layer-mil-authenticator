package it.gov.pagopa.atmlayer.service.milauthenticator.service;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.KeyToken;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.Token;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;

public interface TokenService {

    Uni<TokenDTO> getToken(AuthParameters authParameters);

    Uni<Token> generateToken(AuthParameters authParameters, KeyToken keyToken);
}
