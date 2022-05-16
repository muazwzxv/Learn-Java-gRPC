package com.muazwzxv.server.metadata;

import io.grpc.*;

import java.util.Objects;

public class AuthInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String clientToken = metadata.get(ServerConstant.getUserToken());
        if (!validate(clientToken))
            serverCall.close(Status.UNAUTHENTICATED.withDescription("Invalid token"), metadata);

        UserRole userRole = this.extractUserRole(clientToken);
        // Store user Role in ctx
        Context ctx = Context.current().withValue(
                ServerConstant.getCtxUserRole(),
                userRole
        );
        return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
//        return serverCallHandler.startCall(serverCall, metadata);
    }

    private boolean validate(String token) {
        if (Objects.nonNull(token) && token.equals("User-Secret-test"))
            return true;

        return false;
    }

    private UserRole extractUserRole(String token) {
        return token.endsWith("prime") ? UserRole.PRIME_USER : UserRole.REGULAR_USER;
    }
}
