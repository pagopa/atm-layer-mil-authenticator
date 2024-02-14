package it.gov.pagopa.atmlayer.service.milauthenticator.configuration;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Provider
@Slf4j
public class HttpRequestLogger implements ContainerRequestFilter {
    public void logRequest(ContainerRequestContext requestContext) {
        log.info("====================================request started with transactionId= {}, URI : {}, Method : {}", requestContext.getHeaderString("TransactionId"), requestContext.getUriInfo().getAbsolutePath(), requestContext.getMethod());
        UriInfo uriInfo = requestContext.getUriInfo();
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        log.info("URI info:");
        for (String key : queryParams.keySet()) {log.info(key + ": " + queryParams.get(key));}
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        LocalDateTime timestampStart = LocalDateTime.now();
        log.info("Request started at : {}", timestampStart);
        requestContext.setProperty("timestampStart",timestampStart);
        logRequest(requestContext);
    }
}
