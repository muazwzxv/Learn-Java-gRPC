package com.muazwzxv.client.deadline;

import io.grpc.*;

public class DeadlineInterceptor implements ClientInterceptor {
    public DeadlineInterceptor() {
        super();
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        return null;
    }
}
