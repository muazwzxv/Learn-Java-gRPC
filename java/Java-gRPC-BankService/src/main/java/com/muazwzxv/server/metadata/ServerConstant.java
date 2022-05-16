package com.muazwzxv.server.metadata;

import io.grpc.Context;
import io.grpc.Metadata;

public class ServerConstant {

    private static final Metadata.Key<String> TOKEN = Metadata.Key
            .of("client-token", Metadata.ASCII_STRING_MARSHALLER);

    private static final Metadata.Key<String> USER_TOKEN = Metadata.Key
            .of("user-token", Metadata.ASCII_STRING_MARSHALLER);

    private static final Context.Key<UserRole> CTX_USER_ROLE = Context.key("user-role");


    public static Metadata.Key<String> getServerToken() {
        return TOKEN;
    }

    public static Metadata.Key<String> getUserToken() {
        return USER_TOKEN;
    }

    public static Context.Key<UserRole> getCtxUserRole() {
        return CTX_USER_ROLE;
    }
}
