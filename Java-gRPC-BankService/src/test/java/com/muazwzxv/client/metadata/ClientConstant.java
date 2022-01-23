package com.muazwzxv.client.metadata;

import io.grpc.Metadata;

public class ClientConstant {
    private static final Metadata METADATA = new Metadata();
    public static final Metadata.Key<String> USER_TOKEN = Metadata.Key.of("user-token", Metadata.ASCII_STRING_MARSHALLER);

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
