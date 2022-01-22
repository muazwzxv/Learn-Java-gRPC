package com.muazwzxv.server.metadata;

import io.grpc.*;

import java.util.Objects;

public class AuthInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String clientToken = metadata.get(ServerConstant.getServerToken());
        if (!validate(clientToken))
            serverCall.close(Status.UNAUTHENTICATED.withDescription("Invalid token"), metadata);

        return serverCallHandler.startCall(serverCall, metadata);
    }

    private boolean validate(String token) {
        if (Objects.nonNull(token) && token.equals("bank-client-secret"))
            return true;

        return false;
    }
}
