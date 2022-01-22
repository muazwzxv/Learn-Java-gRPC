package com.muazwzxv.server.metadata;

import io.grpc.Metadata;

public class ServerConstant {
    private static final Metadata.Key<String> TOKEN = Metadata.Key
            .of("client-token", Metadata.ASCII_STRING_MARSHALLER);


    public static Metadata.Key<String> getServerToken() {
        return TOKEN;
    }
}
