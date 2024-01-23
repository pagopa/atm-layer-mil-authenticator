package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.TokenService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TokenResourceTest {

    @Inject
    TokenService tokenService;

    @Inject
    TokenResource tokenResource;

//    @Test
//    void testGetToken() {
//        AuthParameters authParameters = AuthParameters.builder()
//                .acquirerId("acquirer")
//                .channel("channel")
//                .terminalId("terminal")
//                .transactionId("transaction")
//                .build();
//        TokenDTO expectedTokenDTO = new TokenDTO();
//        when(tokenService.getToken(eq(authParameters))).thenReturn(Uni.createFrom().item(expectedTokenDTO));
//
//        Uni<TokenDTO> result = tokenResource.getToken("acquirer", "channel", "terminal", "transaction");
//
//        assertEquals(expectedTokenDTO, result.await().indefinitely());
//
//        verify(tokenService, times(1)).getToken(eq(authParameters));
//    }

}
