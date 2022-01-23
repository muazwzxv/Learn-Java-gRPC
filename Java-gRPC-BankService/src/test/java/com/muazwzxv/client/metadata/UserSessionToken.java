package com.muazwzxv.client.metadata;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;

import java.util.concurrent.Executor;

public class UserSessionToken extends CallCredentials {

    private String jwt;

    public UserSessionToken(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
        // This method should be asynchronous
        executor.execute(() -> {
            // Assignment jwt to metadata goes here
            Metadata data = new Metadata();
            data.put(ClientConstant.USER_TOKEN, this.jwt);

            metadataApplier.apply(data);
            metadataApplier.fail(Status.UNAVAILABLE);
        });
    }

    @Override
    public void thisUsesUnstableApi() {

    }
}
