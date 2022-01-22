package com.muazwzxv.client.metadata;

import io.grpc.Metadata;

public class ClientConstant {
    private static final Metadata METADATA = new Metadata();

    static {
        METADATA.put(
                Metadata.Key.of("client-token", Metadata.ASCII_STRING_MARSHALLER),
                "Bank-Client-Secret"
        );
    }

    public static Metadata getClientToken() {
        return METADATA;
    }
}
