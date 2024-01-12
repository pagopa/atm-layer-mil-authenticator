package it.gov.pagopa.atmlayer.service.milauthenticator.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyToken {

    private String requestId;

    private String terminalId;

    private String acquirerId;

    private String channel;

    private String transactionId;

    @Override
    public String toString() {
        StringBuilder keyTokenBuilder = new StringBuilder();

        appendIfNotNull(keyTokenBuilder, acquirerId);
        appendIfNotNull(keyTokenBuilder, channel);
        appendIfNotNull(keyTokenBuilder, terminalId);
        appendIfNotNull(keyTokenBuilder, transactionId);
        appendIfNotNull(keyTokenBuilder, requestId);

        return keyTokenBuilder.toString();
    }

    private void appendIfNotNull(StringBuilder builder, String value) {
        if (value != null && !value.isEmpty()) {
            if (builder.length() > 0) {
                builder.append("_");
            }
            builder.append(value);
        }
    }
}