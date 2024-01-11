package it.gov.pagopa.atmlayer.service.milauthenticator.service;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import jakarta.ws.rs.core.Response;

public interface TokenService {

    String getToken();

    Uni<Response> generateToken(AuthParameters authParameters);
}
