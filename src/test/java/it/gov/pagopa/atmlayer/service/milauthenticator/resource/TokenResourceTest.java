package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.*;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.TokenService;
import jakarta.ws.rs.HeaderParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class TokenResourceTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TokenResource tokenResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetToken() {
        // Arrange
        AuthParameters authParameters = AuthParameters.builder()
                .acquirerId("acquirer")
                .channel("channel")
                .terminalId("terminal")
                .transactionId("transaction")
                .build();
        TokenDTO expectedTokenDTO = new TokenDTO();
        when(tokenService.getToken(eq(authParameters))).thenReturn(Uni.createFrom().item(expectedTokenDTO));

        // Act
        Uni<TokenDTO> result = tokenResource.getToken("acquirer", "channel", "terminal", "transaction");

        // Assert
        assertEquals(expectedTokenDTO, result.await().indefinitely());
    }

    @Test
    void testCreateToken() {
        // Arrange
        AuthParameters authParameters = AuthParameters.builder()
                .acquirerId("acquirer")
                .channel("channel")
                .terminalId("terminal")
                .fiscalCode("fiscalCode")
                .transactionId("transaction")
                .build();
        TokenDTO expectedTokenDTO = new TokenDTO();
        when(tokenService.generateToken(eq(authParameters))).thenReturn(Uni.createFrom().item(expectedTokenDTO));

        // Act
        Uni<TokenDTO> result = tokenResource.createToken("acquirer", "channel", "terminal", "fiscalCode", "transaction");

        // Assert
        assertEquals(expectedTokenDTO, result.await().indefinitely());
    }

    @Test
    void testDeleteToken() {
        // Arrange
        AuthParameters authParameters = AuthParameters.builder()
                .acquirerId("acquirer")
                .channel("channel")
                .terminalId("terminal")
                .transactionId("transaction")
                .build();
        when(tokenService.deleteToken(eq(authParameters))).thenReturn(Uni.createFrom().nullItem());

        // Act
        Uni<Void> result = tokenResource.deleteToken("acquirer", "channel", "terminal", "transaction");

        // Assert
        result.await().indefinitely(); // Ensure no exceptions are thrown
    }

    // Add more test methods for error cases, validations, etc.
}
