package it.gov.pagopa.atmlayer.service.milauthenticator.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.AuthParameters;
import it.gov.pagopa.atmlayer.service.milauthenticator.model.TokenDTO;
import it.gov.pagopa.atmlayer.service.milauthenticator.service.TokenService;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class TokenResourceTest {
    @InjectMock
    TokenService tokenService;

    @Test
    void testGetToken() {
        TokenDTO expectedTokenDTO = new TokenDTO();
        when(tokenService.getToken(any(AuthParameters.class))).thenReturn(Uni.createFrom().item(expectedTokenDTO));
        given()
                .header("AcquirerId", "acquirerTest")
                .header("Channel", "channelTest")
                .header("TerminalId", "terminalTest")
                .header("TransactionId", "transactionTest")
                .when().get("/api/v1/mil-authenticator/token")
                .then()
                .statusCode(200);
    }

    @Test
    void testCreateToken() {
        TokenDTO expectedTokenDTO = new TokenDTO();
        expectedTokenDTO.setAccessToken("expectedToken");
        when(tokenService.generateToken(any(AuthParameters.class))).thenReturn(Uni.createFrom().item(expectedTokenDTO));
        given()
                .header("AcquirerId", "acquirerTest")
                .header("Channel", "channelTest")
                .header("TerminalId", "terminalTest")
                .header("FiscalCode", "fiscalCodeTest")
                .header("TransactionId", "transactionTest")
                .when().post("/api/v1/mil-authenticator/token")
                .then()
                .statusCode(200);
    }

    @Test
    void testDeleteToken() {
        TokenDTO expectedTokenDTO = new TokenDTO();
        expectedTokenDTO.setAccessToken("expectedToken");
        when(tokenService.deleteToken(any(AuthParameters.class))).thenReturn(Uni.createFrom().voidItem());
        given()
                .header("AcquirerId", "acquirerTest")
                .header("Channel", "channelTest")
                .header("TerminalId", "terminalTest")
                .header("TransactionId", "transactionTest")
                .when().delete("/api/v1/mil-authenticator/token")
                .then()
                .statusCode(204);
    }
}
